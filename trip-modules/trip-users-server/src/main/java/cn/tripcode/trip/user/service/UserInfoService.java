package cn.tripcode.trip.user.service;

import cn.tripcode.trip.user.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserInfoService extends IService<UserInfo> {
    /**
     * 根据手机号查询用户信息
     * @param phone 手机号
     * @return 用户信息
     */
    UserInfo findByPhone(String phone);
}
