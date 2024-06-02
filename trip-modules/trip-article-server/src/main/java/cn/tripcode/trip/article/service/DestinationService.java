package cn.tripcode.trip.article.service;

import cn.tripcode.trip.article.domain.Destination;
import cn.tripcode.trip.article.qo.DestinationQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DestinationService extends IService<Destination> {
    List<Destination> getDestinationByRegionId(Long regionId);

    Page<Destination> pageList(DestinationQuery query);

    List<Destination> findToasts(Long destId);

    List<Destination> findDestsByRid(Long rid);
}
