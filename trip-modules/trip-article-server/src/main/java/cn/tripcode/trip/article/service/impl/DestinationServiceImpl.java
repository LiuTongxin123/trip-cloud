package cn.tripcode.trip.article.service.impl;

import cn.tripcode.trip.article.mapper.DestinationMapper;
import cn.tripcode.trip.article.domain.Destination;
import cn.tripcode.trip.article.domain.Region;
import cn.tripcode.trip.article.qo.DestinationQuery;
import cn.tripcode.trip.article.service.DestinationService;
import cn.tripcode.trip.article.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {
    private final RegionService regionService;
    private final ThreadPoolExecutor bussinessThreadPoolExecutor;

    public DestinationServiceImpl(RegionService regionService, ThreadPoolExecutor threadPoolExecutor) {
        this.regionService = regionService;
        this.bussinessThreadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public List<Destination> getDestinationByRegionId(Long regionId) {
        Region region = regionService.getById(regionId);
        if(region == null){
            return Collections.emptyList();
        }
        List<Long> ids=region.parseRefIds();
        if(ids.size()==0){
            return Collections.emptyList();
        }
        return super.listByIds(ids);
    }

    @Override
    public Page<Destination> pageList(DestinationQuery query) {
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        wrapper.isNull(query.getParentId()==null, "parent_id");
        wrapper.eq(query.getParentId()!=null, "parent_id", query.getParentId());
        wrapper.like(StringUtils.isNotBlank(query.getKeyword()), "name", query.getKeyword());
        return super.page(new Page<>(query.getCurrent(),query.getSize()), wrapper);
    }

    @Override
    public List<Destination> findToasts(Long destId) {
        List<Destination> destinations = new ArrayList<>();
        while(destId!=null){
            Destination dest = super.getById(destId);
            if(dest==null) {
                break;
            }
            destinations.add(dest);
            destId = dest.getParentId();
        }
        Collections.reverse(destinations);
        return destinations;
    }

    @Override
    public List<Destination> findDestsByRid(Long rid) {
        List<Destination> destinations = new ArrayList<>();
        if(rid<0){
            destinations=this.getBaseMapper().selectHotListByRid(rid,Collections.emptyList());
        }
        else{
            Region region = regionService.getById(rid);
            if(region==null) {
                return Collections.emptyList();
            }
            destinations=this.getBaseMapper().selectHotListByRid(rid,region.parseRefIds());
        }
        //创建一个倒计时对象 CountDownLatch, 设置倒计时的值为目的地数量
        CountDownLatch countDownLatch=new CountDownLatch(destinations.size());
        for(Destination dest:destinations){
            bussinessThreadPoolExecutor.execute(()-> {
                List<Destination> children = dest.getChildren();
                // 2. 倒计时数量 -1
                countDownLatch.countDown();
            });
        }
        //3. 等待倒计时结束
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destinations;
    }
}
