package cn.tripcode.trip.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String phone;
    private String nickname;
    private String password;
    private String verifyCode;

}
