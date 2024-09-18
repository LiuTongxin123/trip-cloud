package cn.tripcode.trip.data.mapper;

import cn.tripcode.trip.article.domain.Strategy;
import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.domain.StrategyRank;
import cn.tripcode.trip.article.vo.StrategyCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {
    List<StrategyRank> selectStrategyRankByAbroad(@Param("abroad") Integer abroad);
    List<StrategyRank> selectStrategyRankHotList();
}
