package com.onlineauctions.onlineauctions.controller;

import com.onlineauctions.onlineauctions.annotation.Permission;
import com.onlineauctions.onlineauctions.annotation.RequestPage;
import com.onlineauctions.onlineauctions.annotation.RequestToken;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.respond.PageInfo;
import com.onlineauctions.onlineauctions.pojo.respond.Result;
import com.onlineauctions.onlineauctions.pojo.type.Role;
import com.onlineauctions.onlineauctions.pojo.user.User;
import com.onlineauctions.onlineauctions.service.UserService;
import com.onlineauctions.onlineauctions.service.redis.JwtService;
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
    @Permission(Role.ADMIN)
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
    public Result<String> updateInfo(@RequestToken("username")long username, @RequestBody @Validated User user){
        user.setUsername(username);
        String jwt = userService.updateInfo(user);
        return jwt != null?
                Result.success("上传用户信息成功",jwt) :
                Result.failure("上传用户信息失败 密码错误请重试");
    }

    /**
     * 获取用户列表
     *
     * @param pageInfo 分页信息
     * @return 返回Result对象，其中包含分页列表信息
     */
    @GetMapping("/list")
    @Permission(Role.ADMIN)
    public Result<PageList<User>> userList(@RequestPage PageInfo pageInfo) {
        // 调用服务层方法获取用户列表
        PageList<User> pageList = userService.getUserList(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getFilter());
        // 返回结果
        return pageList !=null?Result.success(pageList):Result.failure("获取用户列表失败");
    }




}
