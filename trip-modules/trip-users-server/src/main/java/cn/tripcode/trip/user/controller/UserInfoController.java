package cn.tripcode.trip.user.controller;

import cn.tripcode.trip.user.service.UserInfoService;

import cn.tripcode.trip.core.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
