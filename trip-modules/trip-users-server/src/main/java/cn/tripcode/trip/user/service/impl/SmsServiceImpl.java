package cn.tripcode.trip.user.service.impl;
import cn.tripcode.trip.core.exception.BusinessException;
import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.redis.utils.RedisCache;
import cn.tripcode.trip.user.redis.key.UserRedisKeyPrefix;
import cn.tripcode.trip.user.service.SmsService;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Value("${aliyun.dysms.templateCode}")
    private String templateCode;
    @Value("${aliyun.dysms.sign}")
    private String sign;

    private final RedisCache redisCache;
    private final Client smsClient;

    public SmsServiceImpl(RedisCache redisCache, Client smsClient) {
        this.redisCache = redisCache;
        this.smsClient = smsClient;
    }

    @Override
    public void registerSmsSend(String phone) {
        // 2. 生成验证码(纯数字, 字母+数字)
        String code = this.generateVerifyCode("MATH", 6);
        // 4. 调用第三方接口, 发送验证码
        try {
            this.send(phone, code);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 3. 将验证保存起来, 半小时内有效
        redisCache.setCacheObject(UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING, code,phone);
    }

    private void send(String phone, String code) throws Exception {
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest =
                new com.aliyun.dysmsapi20170525.models.SendSmsRequest().setTemplateCode(templateCode)
                        .setTemplateParam("{\"code\":\"" + code + "\"}")
                        .setPhoneNumbers(phone)
                        .setSignName(sign);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = smsClient.sendSmsWithOptions(sendSmsRequest, runtime);
        ObjectMapper mapper = new ObjectMapper();
        SendSmsResponseBody body = response.body;
        String respJson = mapper.writeValueAsString(body);
        log.info("[短信服务] 阿里云发送短信响应结果: {}", respJson);
        if (!"OK".equals(body.code)) {
            throw new BusinessException(R.CODE_SMS_ERROR,body.message);

        }
    }

    private String generateVerifyCode(String type, int len) {
        StringBuilder code = new StringBuilder();
        if ("MATH".equalsIgnoreCase(type)) {
            Random random = new Random();
            for (int i = 0; i < len; i++) {
                code.append(random.nextInt(10));
            }
        } else {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            code = new StringBuilder(uuid.substring(0, len));
        }
        log.info("[短信服务] 生成验证码 ====> type={}, len={}, code={}", type, len, code);
        return code.toString();
    }
}
