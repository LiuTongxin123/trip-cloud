package cn.tripcode.trip.article.service;

import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StrategyCatalogService extends IService<StrategyCatalog> {
    List<StrategyCatalogGroup> findGroupList();
}
