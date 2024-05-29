package cn.tripcode.trip.user.interceptor;

import cn.tripcode.trip.core.exception.BusinessException;
import cn.tripcode.trip.redis.utils.RedisCache;
import cn.tripcode.trip.user.config.JwtProperties;
import cn.tripcode.trip.user.redis.key.UserRedisKeyPrefix;
import cn.tripcode.trip.user.vo.LoginUser;
import io.jsonwebtoken.*;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final RedisCache redisCache;
    private final JwtProperties  jwtProperties;

    public LoginInterceptor(RedisCache redisCache, JwtProperties jwtProperties) {
        this.redisCache = redisCache;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader(LoginUser.TOKEN_HEADER);
        System.out.println(token);
        try {
            Jws<Claims> jwt= Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            Claims claims =  jwt.getBody();
            String uuid = (String) claims.get(LoginUser.LOGIN_USER_REDIS_UUID);
            //从redis中拿到登录数据说明没过期
            LoginUser loginUser =redisCache.getCacheObject(UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid));
            long loginTime;
            if(loginUser==null){
                 throw new BusinessException(401,"token已过期");
            }
            else if(loginUser.getExpireTime()-(loginTime=System.currentTimeMillis())<=LoginUser.TWENTY_MINUTES_MILLISECONDS){
                loginUser.setLoginTime(loginTime);
                long expireTime = loginTime+(jwtProperties.getExpireTime()*LoginUser.MINUTES_MILLISECONDS);
                loginUser.setExpireTime(expireTime);
                redisCache.setCacheObject(UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid),loginUser,expireTime, TimeUnit.MICROSECONDS);
            }

        }catch (Exception e){
            throw new BusinessException(401,"用户未认证");
        }
        return true;
    }
}
