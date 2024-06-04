package cn.tripcode.trip.article.service.impl;

import cn.tripcode.trip.article.domain.StrategyCatalog;
import cn.tripcode.trip.article.mapper.StrategyCatalogMapper;
import cn.tripcode.trip.article.service.StrategyCatalogService;
import cn.tripcode.trip.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog> implements StrategyCatalogService {

    @Override
    public List<StrategyCatalogGroup> findGroupList() {
        return getBaseMapper().selectGroupList();
    }
}
