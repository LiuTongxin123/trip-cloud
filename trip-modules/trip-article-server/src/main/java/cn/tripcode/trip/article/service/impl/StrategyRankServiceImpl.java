package cn.tripcode.trip.article.service.impl;

import cn.tripcode.trip.article.domain.Region;
import cn.tripcode.trip.article.domain.StrategyRank;
import cn.tripcode.trip.article.mapper.RegionMapper;
import cn.tripcode.trip.article.mapper.StrategyRankMapper;
import cn.tripcode.trip.article.service.RegionService;
import cn.tripcode.trip.article.service.StrategyRankService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyRankServiceImpl extends ServiceImpl<StrategyRankMapper, StrategyRank> implements StrategyRankService {

    @Override
    public List<StrategyRank> selectLastRanksByType(int type) {
        QueryWrapper<StrategyRank> wrapper=new QueryWrapper<StrategyRank>()
                .eq("type",type)
                .orderByDesc("statis_time")
                .last("limit 10");
        return list(wrapper);
    }
}
