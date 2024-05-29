package cn.tripcode.trip.user.controller;

import cn.tripcode.trip.user.domain.UserInfo;
import cn.tripcode.trip.user.service.UserInfoService;

import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.user.vo.RegisterRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/phone/exists")
    public R<Boolean> checkPhoneExists(String phone){
        return R.ok(userInfoService.findByPhone(phone)!=null);
    }
    @PostMapping("/register")
    public R<String> register(RegisterRequest registerRequest){
        userInfoService.register(registerRequest);
        return R.ok();

    }
    @PostMapping(value = "/login")
    public R<Map<String,Object>> login(String username,  String password){
        Map<String,Object> map =  userInfoService.login(username,password);
        return R.ok(map);
    }

}
