package cn.tripcode.trip.article.feign;

import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("users-service")
public interface UserInfoFeignService {
    @GetMapping("/user/getById")
    R<UserInfoDTO> getById(@RequestParam Long id);
    @GetMapping("/user/favor/strategies")
    R<List<Long>> getFavorStrategyIdList(@RequestParam Long userId);
}
