package cn.tripcode.trip.user.controller;

import cn.tripcode.trip.auth.anno.RequireLogin;
import cn.tripcode.trip.user.domain.UserInfo;
import cn.tripcode.trip.user.dto.UserInfoDTO;
import cn.tripcode.trip.user.service.UserInfoService;

import cn.tripcode.trip.core.utils.R;
import cn.tripcode.trip.user.vo.RegisterRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }
    @RequireLogin
    @PostMapping("/favor/strategies")
    public R<Boolean> favorStrategy(Long sid){
        return R.ok(userInfoService.favorStrategy(sid));
    }
    @GetMapping("/favor/strategies")
    public R<List<Long>> getFavorStrategyIdList(Long userId){
        return R.ok(userInfoService.getFavorStrategyIdList(userId));
    }
    @GetMapping("/phone/exists")
    public R<Boolean> checkPhoneExists(String phone){
        return R.ok(userInfoService.findByPhone(phone)!=null);
    }
    @PostMapping("/register")
    public R<String> register(RegisterRequest registerRequest){
        userInfoService.register(registerRequest);
        return R.ok();

    }
    @PostMapping(value = "/login")
    public R<Map<String,Object>> login(String username,  String password){
        Map<String,Object> map =  userInfoService.login(username,password);
        return R.ok(map);
    }
    @GetMapping("/getById")
    public R<UserInfoDTO> getById(Long id){
        return R.ok(userInfoService.getDtoById(id));
    }
    @GetMapping
    public R<List<UserInfoDTO>> findList(Integer current,Integer limit){
        int offSet = (current-1)*limit;
        QueryWrapper<UserInfo> queryWrapper =
                new QueryWrapper<UserInfo>().last("limit "+offSet+", "+limit);

        List<UserInfoDTO> dtoList = userInfoService.list(queryWrapper)
                .stream()
                .map(UserInfo::toDto)
                .collect(Collectors.toList());
        return R.ok(dtoList);
    }
    @GetMapping("/findByDestName")
    public R<List<UserInfo>> findUserByDestName(@RequestParam String destName) {
        return R.ok(userInfoService.list(new QueryWrapper<UserInfo>().eq("city", destName)));
    }

}
