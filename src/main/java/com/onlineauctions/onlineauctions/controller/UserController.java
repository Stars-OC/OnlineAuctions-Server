package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.pojo.Result;
import com.onlineauctions.onlineauctions.pojo.user.User;
import com.onlineauctions.onlineauctions.service.UserService;
import com.onlineauctions.onlineauctions.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;



    /**
     * 刷新token
     *
     * @param token 要刷新的token
     * @return 刷新结果
     */
    @GetMapping("/refresh_token")
    public Result<String> refreshToken(@RequestHeader String token){

        return Result.success("刷新token成功",jwtService.uploadJwtByToken(token));

    }


    /**
     * 上传用户信息
     *
     * @param user 用户信息
     * @return 结果对象
     */
    @PostMapping("/update/info")
    public Result<String> updateInfo(@RequestBody @Validated User user){

        String jwt = userService.updateInfo(user);
        return jwt != null?
                Result.success("上传用户信息成功",jwt) :
                Result.failure("上传用户信息失败 密码错误请重试");
    }





}
