package cn.tripcode.trip.article.mapper;

import cn.tripcode.trip.article.domain.Strategy;
import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.domain.StrategyTheme;
import cn.tripcode.trip.article.vo.StrategyCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {
    List<StrategyCatalog> selectGroupsByDestId(Long destId);

    List<StrategyCondition> selectDestCondition(int abroad);

    List<StrategyCondition> selectThemeCondition();
}
