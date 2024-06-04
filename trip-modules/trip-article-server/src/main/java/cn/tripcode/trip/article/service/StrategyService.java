package cn.tripcode.trip.article.service;

import cn.tripcode.trip.article.domain.Strategy;
import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.domain.StrategyContent;
import cn.tripcode.trip.article.domain.StrategyTheme;
import cn.tripcode.trip.article.qo.StrategyQuery;
import cn.tripcode.trip.article.vo.StrategyCondition;
import cn.tripcode.trip.core.qo.QueryObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StrategyService extends IService<Strategy> {
    List<StrategyCatalog> findGroupsByDestId(Long destId);

    StrategyContent getContentById(Long id);

    List<Strategy> findViewnumTop3ByDestId(Long destId);

    Page<Strategy> pageList(StrategyQuery qo);

    List<StrategyCondition> findDestCondition(int abroad);

    List<StrategyCondition> findthemeCondition();
}
