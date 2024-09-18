package cn.tripcode.trip.article.service;

import cn.tripcode.trip.article.domain.Region;
import cn.tripcode.trip.article.domain.StrategyRank;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyRankService extends IService<StrategyRank> {

    List<StrategyRank> selectLastRanksByType(int type);
}
