package cn.tripcode.trip.search.feign;

import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("users-service")
public interface UserInfoFeignService {

    /**
     * 如果 Feign 发起远程调用后, 接收的类型没有明确表示具体类型是什么
     * Feign 会将返回的 JSON 结构转换为一个 LinkedHashMap 对象
     */
    @GetMapping("/user")
    R<List<Object>> findList(@RequestParam Integer current, @RequestParam Integer limit);

    @GetMapping("/user/findByDestName")
    R<List<UserInfoDTO>> findUserByDestName(@RequestParam String destName);

    @GetMapping("/user/getById")
    R<UserInfoDTO> getById(@RequestParam String id);
}
