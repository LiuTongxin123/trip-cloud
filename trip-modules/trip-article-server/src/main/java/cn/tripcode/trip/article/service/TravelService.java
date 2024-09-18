package cn.tripcode.trip.article.service;

import cn.tripcode.trip.article.domain.Travel;
import cn.tripcode.trip.article.qo.TravelQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TravelService extends IService<Travel> {

    Page<Travel> pageList(TravelQuery query);
}
