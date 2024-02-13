package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestPage;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.request.UsernameAndPWD;
import com.onlineauctions.onlineauctions.pojo.respond.PageInfo;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.pojo.user.User;
import com.onlineauctions.onlineauctions.service.auth.AuthService;
import com.onlineauctions.onlineauctions.service.redis.JwtService;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    /**
     * 用户登录
     *
     * @param usernameAndPWD 用户名和密码
     * @return 登录成功返回JWT，登录失败返回"账号密码错误"
     */
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody UsernameAndPWD usernameAndPWD){

        String jwt = authService.login(usernameAndPWD);
        return jwt != null? Result.success("登录成功",jwt) : Result.failure("账号密码错误");

    }



    /**
     * 用户注册
     * POST /user/register
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@Validated @RequestBody User user){

        String jwt = authService.register(user);

        return jwt != null ? Result.success("注册成功",jwt) : Result.failure("注册失败");

    }


    /**
     * 验证Token
     * @return 验证结果
     */
    @GetMapping("/verify")
    public Result<String> verify(){
        return Result.success("token验证成功");
    }

    /**
     * 退出登录
     *
     * @return 登录结果
     */
    @GetMapping("/logout")
    @Permission({Role.USER,Role.AUDIT_ADMIN})
    public Result<String> logout(@NotEmpty(message = "用户名不能为空") String username){

        jwtService.deleteJwtByUsername(username);

        return Result.success("退出成功");
    }



}
