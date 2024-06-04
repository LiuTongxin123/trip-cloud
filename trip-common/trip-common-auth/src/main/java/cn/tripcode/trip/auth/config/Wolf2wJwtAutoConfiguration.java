package cn.tripcode.trip.auth.config;

import cn.tripcode.trip.auth.interceptor.LoginInterceptor;
import cn.tripcode.trip.auth.utils.SpringContextUtil;
import cn.tripcode.trip.redis.utils.RedisCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
/**
 * \@Import 注解等同于以前 xml 文件中的 \<import resource="applicationContext.xml"/>
 */
//@Import(WebConfig.class)
@EnableConfigurationProperties(JwtProperties.class)
public class Wolf2wJwtAutoConfiguration {
    @Bean
    public LoginInterceptor loginInterceptor(RedisCache redisCache,JwtProperties jwtProperties) {
        return new LoginInterceptor(redisCache,jwtProperties);
    }
    @Bean
    public WebConfig webConfig(RedisCache redisCache,JwtProperties jwtProperties){
        return new WebConfig(loginInterceptor(redisCache,jwtProperties));
    }
    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
