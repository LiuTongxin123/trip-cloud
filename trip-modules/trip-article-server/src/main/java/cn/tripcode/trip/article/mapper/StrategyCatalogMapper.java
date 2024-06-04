package cn.tripcode.trip.article.mapper;

import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface StrategyCatalogMapper extends BaseMapper<StrategyCatalog> {
    List<StrategyCatalogGroup> selectGroupList();
}
