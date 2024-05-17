package cn.tripcode.trip.user.service.impl;

import cn.tripcode.trip.user.mapper.UserInfoMapper;
import cn.tripcode.trip.user.service.UserInfoService;
import cn.tripcode.trip.user.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

}
