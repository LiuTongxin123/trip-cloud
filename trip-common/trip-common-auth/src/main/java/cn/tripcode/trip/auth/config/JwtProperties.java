package cn.tripcode.trip.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;




@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private Integer expireTime;
    private String secret;
}
