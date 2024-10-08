package cn.tripcode.trip.article.service.impl;

import cn.tripcode.trip.article.domain.Banner;
import cn.tripcode.trip.article.mapper.BannerMapper;
import cn.tripcode.trip.article.service.BannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> findByType(Integer type) {
        return list(
                new QueryWrapper<Banner>()
                        .eq("type", type)
                        .eq("state", Banner.STATE_NORMAL)
                        .orderByAsc("seq")
        );
    }
}
