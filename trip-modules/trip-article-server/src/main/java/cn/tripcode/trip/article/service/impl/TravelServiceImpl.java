package cn.tripcode.trip.article.service.impl;

import cn.tripcode.trip.article.domain.Travel;
import cn.tripcode.trip.article.domain.TravelContent;
import cn.tripcode.trip.article.feign.UserInfoFeignService;
import cn.tripcode.trip.article.mapper.TravelContentMapper;
import cn.tripcode.trip.article.mapper.TravelMapper;
import cn.tripcode.trip.article.qo.TravelQuery;
import cn.tripcode.trip.article.service.TravelService;
import cn.tripcode.trip.article.vo.TravelRange;
import cn.tripcode.trip.auth.utils.AuthenticationUtils;
import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.user.dto.UserInfoDTO;
import cn.tripcode.trip.user.vo.LoginUser;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {
    private final UserInfoFeignService userInfoFeignService;
    private final ThreadPoolExecutor bussinessThreadPoolExecutor;
    private final TravelContentMapper travelContentMapper;
    public TravelServiceImpl(UserInfoFeignService userInfoFeignService, ThreadPoolExecutor bussinessThreadPoolExecutor, TravelContentMapper travelContentMapper) {
        this.userInfoFeignService = userInfoFeignService;
        this.bussinessThreadPoolExecutor = bussinessThreadPoolExecutor;
        this.travelContentMapper = travelContentMapper;
    }

    @Override
    public Travel getById(Serializable id){
        Travel travel = super.getById(id);
        if (travel == null) {
            return null;
        }
        TravelContent content=travelContentMapper.selectById(id);
        travel.setContent(content);
        R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
        UserInfoDTO dto=result.checkAndGet();
        travel.setAuthor(dto);
        return travel;
    }
    @Override
    public Page<Travel> pageList(TravelQuery query) {
        QueryWrapper<Travel> wrapper = Wrappers.<Travel>query()
                .eq(query.getDestId() != null, "dest_id", query.getDestId());

        if (query.getTravelTimeRange() != null) {
            TravelRange range = query.getTravelTimeRange();
            wrapper.between("MONTH(travel_time)", range.getMin(), range.getMax());
        }

        if (query.getCostRange() != null) {
            TravelRange range = query.getCostRange();
            wrapper.between("avg_consume", range.getMin(), range.getMax());
        }

        if (query.getDayRange() != null) {
            TravelRange range = query.getDayRange();
            wrapper.between("day", range.getMin(), range.getMax());
        }
        // 排序
        wrapper.orderByDesc(query.getOrderBy());
        LoginUser user = AuthenticationUtils.getUser();
        if (user == null) {
            // 游客: 只能看公开且已发布的游记
            wrapper.eq("ispublic", Travel.ISPUBLIC_YES)
                    .eq("state", Travel.STATE_RELEASE);
        } else {
            log.info("用户: {}", user.getNickname());
            // 用户: 可以查看公开已发布和自己所有的游记
            // (author_id = #{user.id} or (ispublic = 1 and state = 2))
            wrapper.and(w -> {
                w.eq("author_id", user.getId())
                        .or(ww -> {
                            ww.eq("ispublic", Travel.ISPUBLIC_YES)
                                    .eq("state", Travel.STATE_RELEASE);
                        });
            });
        }
        // 分页
        Page<Travel> page = super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);

        List<Travel> records = page.getRecords();
        // 创建计数器, 等待子线程都执行完成
        CountDownLatch latch = new CountDownLatch(records.size());

        for (Travel travel : records) {
            bussinessThreadPoolExecutor.execute(() -> {
                try {
                    // 查找游记的作者
                    R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
                    if (result.getCode() != R.CODE_SUCCESS) {
                        log.warn("[游记服务] 查询用户作者失败, 返回数据异常: {}", JSON.toJSONString(result));
                        // 数量-1
                        latch.countDown();
                        return;
                    }
                    travel.setAuthor(result.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 数量-1
                    latch.countDown();
                }
            });
        }

        // 返回前等待计数器数值减到0, 也就表示所有子线程都执行结束
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return page;
    }
}
