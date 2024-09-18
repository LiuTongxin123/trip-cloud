package cn.tripcode.trip.data.service.impl;

import cn.tripcode.trip.article.domain.Strategy;
import cn.tripcode.trip.data.mapper.StrategyMapper;
import cn.tripcode.trip.data.service.StrategyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
}
