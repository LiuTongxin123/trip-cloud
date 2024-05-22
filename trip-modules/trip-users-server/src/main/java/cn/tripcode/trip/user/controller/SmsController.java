package cn.tripcode.trip.user.controller;

import cn.tripcode.trip.user.service.SmsService;
import cn.tripcode.trip.core.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {
    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("register")
    public R<?> registerVerifyCode(String phone) {
        // TODO 手机号格式校验
        smsService.registerSmsSend(phone);
        return R.ok();
    }
}
