package cn.tripcode.trip.article.service;

import cn.tripcode.trip.article.domain.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> findByType(Integer type);
}
