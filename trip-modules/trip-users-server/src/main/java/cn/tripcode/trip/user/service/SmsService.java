package cn.tripcode.trip.user.service;

public interface SmsService {
    /**
     * 注册发送短信
     * @param phone 手机好
     */
    void registerSmsSend(String phone);
}
