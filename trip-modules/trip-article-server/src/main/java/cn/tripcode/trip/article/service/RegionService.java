package cn.tripcode.trip.article.service;

import cn.tripcode.trip.article.domain.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RegionService extends IService<Region> {
    List<Region> findHotList();
}
