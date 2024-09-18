package cn.tripcode.trip.user.service;

import cn.tripcode.trip.user.domain.UserInfo;
import cn.tripcode.trip.user.dto.UserInfoDTO;
import cn.tripcode.trip.user.vo.RegisterRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    /**
     * 根据手机号查询用户信息
     * @param phone 手机号
     * @return 用户信息
     */
    UserInfo findByPhone(String phone);

    void register(RegisterRequest registerRequest);

    Map<String, Object> login(String username, String password);

    UserInfoDTO getDtoById(Long id);

    List<Long> getFavorStrategyIdList(Long userId);

    Boolean favorStrategy(Long sid);
}
