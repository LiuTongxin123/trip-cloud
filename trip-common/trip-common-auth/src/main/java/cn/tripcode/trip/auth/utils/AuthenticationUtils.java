package cn.tripcode.trip.auth.utils;

import cn.tripcode.trip.auth.config.JwtProperties;
import cn.tripcode.trip.core.exception.BusinessException;
import cn.tripcode.trip.redis.utils.RedisCache;
import cn.tripcode.trip.user.redis.key.UserRedisKeyPrefix;
import cn.tripcode.trip.user.vo.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
@Slf4j
public abstract class AuthenticationUtils {
    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attr==null){
            throw new BusinessException("该方法只能在SpringMvc环境下使用");
        }
        return attr.getRequest();
    }
    public static String getToken(){
        return getRequest().getHeader(LoginUser.TOKEN_HEADER);
    }
    public static LoginUser getUser() {
        LoginUser cachedUser = USER_HOLDER.get();
        if (cachedUser != null) {
            return cachedUser;
        }

        String token = getToken();
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        JwtProperties jwtProperties = SpringContextUtil.getBean(JwtProperties.class);
        RedisCache redisCache = SpringContextUtil.getBean(RedisCache.class);

        try {
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);

            // 3. 获取 token 中登录时间数据, 判断是否已经过期
            Claims claims = jwt.getBody();
            String uuid = (String) claims.get(LoginUser.LOGIN_USER_REDIS_UUID);
            // 4. 从 redis 中获取数据, 如果能拿到说明没有过期, 如果拿不到, 说明已经过期了
            String userLoginKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid);
            LoginUser user = redisCache.getCacheObject(userLoginKey);
            // 将第一次查询到用户信息保存起来
            USER_HOLDER.set(user);
            return user;
        } catch (Exception e) {
            log.warn("[认证工具] 获取用户信息失败: {}", e.getMessage());
        }

        return null;
    }

    public static void removeUser() {
        USER_HOLDER.remove();
    }

}
