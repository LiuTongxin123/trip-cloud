package cn.tripcode.trip.user.service.impl;

import cn.tripcode.trip.user.mapper.UserInfoMapper;
import cn.tripcode.trip.user.service.UserInfoService;
import cn.tripcode.trip.user.domain.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

    @Override
    public UserInfo findByPhone(String phone) {
        QueryWrapper<UserInfo> wrapper=new QueryWrapper<UserInfo>()
                .eq("phone",phone);
        return getOne(wrapper);
    }
}
