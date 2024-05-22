package cn.tripcode.trip.user.service.impl;

import cn.tripcode.trip.redis.utils.RedisCache;
import cn.tripcode.trip.user.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    private final RedisCache  redisCache;

    public SmsServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public void registerSmsSend(String phone) {
        //TODO 1.验证手机合法性
        //2.生成验证码
        String code= this.generateVerifyCode("LETTER",4);
        //3.将验证码保存
        redisCache.setCacheObject("USERS:REGISTER:VERIFY_CODE:"+phone,code, 30L, TimeUnit.MINUTES);
        //4.调第三方接口发送验证码
    }

    private String generateVerifyCode(String letter, int len) {
        String uuid= UUID.randomUUID().toString().replace("-","");
        String code=uuid.substring(0,len);
        log.info("[验证码] 生成验证码 =====> type={}, len={}, code={}",letter,len,code);
        return code;
    }
}
