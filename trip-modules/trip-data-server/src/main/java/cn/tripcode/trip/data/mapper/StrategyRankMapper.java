package cn.tripcode.trip.data.mapper;
import cn.tripcode.trip.article.domain.StrategyRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface StrategyRankMapper extends BaseMapper<StrategyRank> {
    void batchInsert(List<StrategyRank> strategyRanks);
}
