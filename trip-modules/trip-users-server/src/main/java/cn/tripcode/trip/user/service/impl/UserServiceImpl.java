package cn.tripcode.trip.user.service.impl;

import cn.tripcode.trip.auth.config.JwtProperties;
import cn.tripcode.trip.auth.utils.AuthenticationUtils;
import cn.tripcode.trip.core.exception.BusinessException;
import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.redis.utils.RedisCache;
import cn.tripcode.trip.user.dto.UserInfoDTO;
import cn.tripcode.trip.user.mapper.UserInfoMapper;
import cn.tripcode.trip.user.redis.key.UserRedisKeyPrefix;
import cn.tripcode.trip.user.service.UserInfoService;
import cn.tripcode.trip.user.domain.UserInfo;
import cn.tripcode.trip.user.vo.LoginUser;
import cn.tripcode.trip.user.vo.RegisterRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.tripcode.trip.core.utils.Md5Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author trip
 * @date 2021/1/11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

    private final RedisCache redisCache;
    private final JwtProperties jwtProperties;

    public UserServiceImpl(RedisCache redisCache, JwtProperties jwtProperties) {
        this.redisCache = redisCache;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public UserInfo findByPhone(String phone) {
        QueryWrapper<UserInfo> wrapper=new QueryWrapper<UserInfo>()
                .eq("phone",phone);
        return getOne(wrapper);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        UserInfo userInfo= this.findByPhone(registerRequest.getPhone());
        if(userInfo!=null){
            throw new BusinessException(R.CODE_REGISTER_ERROR,"手机号已经被注册");
        }
        String code=redisCache.getCacheObject(UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING
                .fullKey(registerRequest.getPhone()));
        if(!registerRequest.getVerifyCode().equalsIgnoreCase(code)){
            throw new BusinessException(R.CODE_REGISTER_ERROR,"验证码错误");
        }
        redisCache.deleteObject(UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING
                .fullKey(registerRequest.getPhone()));
        userInfo=this.buildUserInfo(registerRequest);
        String encryptPassword=Md5Utils.getMD5(userInfo.getPassword()+userInfo.getPhone());
        userInfo.setPassword(encryptPassword);
        super.save(userInfo);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        //1.基于用户名查询用户对象，如果为空抛出异常
        UserInfo userInfo=this.findByPhone(username);
        if(userInfo==null){
            throw new BusinessException(R.CODE_LOGIN_ERROR,"用户名或密码错误");
        }
        //2.判断密码是否正确
        String encryptPassword=Md5Utils.getMD5(password+username);
        if(!encryptPassword.equalsIgnoreCase(userInfo.getPassword())){
             throw new BusinessException(R.CODE_LOGIN_ERROR,"用户名或密码错误");
        }
        LoginUser loginUser=new LoginUser();
        BeanUtils.copyProperties(userInfo,loginUser);
        // 当前时间
        long now = System.currentTimeMillis();
        loginUser.setLoginTime(now);
        // 过期时间
        long expireTime = jwtProperties.getExpireTime() * LoginUser.MINUTES_MILLISECONDS;
        long fullExpireTime = now + expireTime;
        loginUser.setExpireTime(fullExpireTime);
        //redis唯一标识
        String uuid=UUID.randomUUID().toString().replace("-","");
        UserRedisKeyPrefix loginInfoString=UserRedisKeyPrefix.USER_LOGIN_INFO_STRING;
        loginInfoString.setTimeout(expireTime);
        loginInfoString.setUnit(TimeUnit.MILLISECONDS);
        redisCache.setCacheObject(loginInfoString,loginUser,uuid);

        //3.生成token
        Map<String, Object> payload=new HashMap<>();
        payload.put(LoginUser.LOGIN_USER_REDIS_UUID,uuid);

        String jwtToken=Jwts.builder().addClaims(payload).signWith(SignatureAlgorithm.HS256,jwtProperties.getSecret() )
                .compact();
        payload.clear();
        payload.put("token",jwtToken);
        payload.put("user",loginUser);
        return payload;
    }

    @Override
    public UserInfoDTO getDtoById(Long id) {
        UserInfo userInfo=super.getById(id);
        if(userInfo!=null){
            UserInfoDTO userInfoDTO=new UserInfoDTO();
            BeanUtils.copyProperties(userInfo,userInfoDTO);
            return userInfoDTO;
        }
        return null;
    }

    @Override
    public List<Long> getFavorStrategyIdList(Long userId) {

        return getBaseMapper().selectFavorStrategyIdList(userId);
    }

    @Override
    public Boolean favorStrategy(Long sid) {
        // 1. 获取当前登录的用户
        LoginUser user = AuthenticationUtils.getUser();
        // 2. 获取当前用户收藏的文章列表
        List<Long> list = this.getFavorStrategyIdList(user.getId());
        // 3. 判断当前用户是否已经收藏过该文章
        if (list.contains(sid)) {
            // 4. 收藏过 => 取消收藏, 数量-1
            getBaseMapper().deleteFavorStrategy(user.getId(), sid);
            redisCache.hashIncrement(UserRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "favornum",
                    -1, sid + "");
            return false;
        }

        // 5. 没收藏 => 收藏, 数量+1
        getBaseMapper().insertFavorStrategy(user.getId(), sid);
        redisCache.hashIncrement(UserRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "favornum",
                1, sid + "");
        return true;
    }

    private UserInfo buildUserInfo(RegisterRequest registerRequest) {
        UserInfo userInfo=new UserInfo();
        BeanUtils.copyProperties(registerRequest,userInfo);
        userInfo.setInfo("这个人很懒，什么都没写。");
        userInfo.setHeadImgUrl("/images/default.jpg");
        userInfo.setState(UserInfo.STATE_NORMAL);
        return userInfo;
    }
}
