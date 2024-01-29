package com.onlineauctions.onlineauctions.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.onlineauctions.onlineauctions.mapper.UserMapper;
import com.onlineauctions.onlineauctions.pojo.request.UsernameAndPWD;
import com.onlineauctions.onlineauctions.pojo.type.LoginWay;
import com.onlineauctions.onlineauctions.pojo.user.User;
import com.onlineauctions.onlineauctions.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;





    /**
     * 判断给定条件在指定数据表中是否存在
     * @param mapper 数据表名
     * @param type 列名
     * @param value 列值
     * @return 存在返回对应对象，否则返回null
     */
    private Object isExists(String mapper, String type, Object value) {
        switch (mapper) {
            case "user":
                return userMapper.selectOne(new QueryWrapper<User>().eq(type, value));
            default:
                return null;
        }
    }

    /**
     * 根据指定的用户名查找用户
     *
     * @param username 要查找的用户名
     * @return 如果找到匹配的用户，则返回该用户对象；否则返回null
     */
    public User findUsername(Long username){
        return (User) isExists("user","username", username);
    }



    /**
     * 用户登录
     *
     * @param usernameAndPWD 用户名和密码
     * @return 登录成功返回JWT，登录失败返回"账号密码错误"
     */
    public String login(UsernameAndPWD usernameAndPWD){

        Long username = usernameAndPWD.getUsername();
        // 根据用户名查找用户
        User user = findUsername(username);

        try {
            // 判断密码是否匹配
            String password = AesUtil.encrypt(usernameAndPWD.getPassword());

            if (password.equals(user.getPassword())){

                // 登录成功，返回JWT
                String jwt = jwtService.createJwt(user);

                log.info("用户 {} 登录成功", username);

                return jwt;
            }
        }catch (NullPointerException e){

            log.warn("用户 {} 登录失败，疑似危险操作", username);
        }

        // 密码不匹配或用户不存在，返回"账号密码错误"
        return null;
    }





    /**
     * 注册用户
     *
     * @param user 要注册的用户对象
     * @return 注册是否成功
     */
    public String register(User user){

        Long username = user.getUsername();
        if (findUsername(username) == null){

            user.setRegisterWay(LoginWay.NORMAL_LOGIN.getCode());

            userMapper.insert(user);

            String jwt = jwtService.createJwt(user);
            return jwt;
        }

        return null;
    }



}
