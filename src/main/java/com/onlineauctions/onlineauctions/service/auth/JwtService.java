package com.onlineauctions.onlineauctions.service.auth;

import com.onlineauctions.onlineauctions.mapper.UserMapper;
import com.onlineauctions.onlineauctions.pojo.user.User;
import com.onlineauctions.onlineauctions.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${user.verifyTime}")
    private int verifyTime;

    // 后面进行区分
    private final String prefix = "token:";

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建JWT并将其与用户名和过期时间一起存入Redis数据库中
     * 也可以当作更新jwt的方法
     *
     * @param user 用户对象
     * @return 生成的JWT
     */
    public String createJwt(User user){
        // 创建JWT
        String jwt = JwtUtil.createJwt(user, verifyTime);

        // 将JWT和用户名以及过期时间存入Redis
        redis.opsForValue().set(user.getUsername().toString() , jwt, verifyTime, TimeUnit.HOURS);

        return jwt;
    }

    /**
     * 通过token上传新的Jwt令牌
     *
     * @param token Jwt令牌
     * @return 新的Jwt令牌
     */
    public String uploadJwtByToken(String token) {

        // 获取用户名
        Long username = JwtUtil.getUsername(token);

        // 根据用户名更新token
        String newToken = uploadJwtByUsername(username);

        // 返回新的Jwt令牌
        return newToken;
    }

    /**
     * 根据用户名获取并返回新的Jwt令牌
     *
     * @param username 用户名
     * @return 新的Jwt令牌，如果用户不存在则返回null
     */
    public String uploadJwtByUsername(Long username) {
        // 根据用户名查询用户信息
        User user = userMapper.selectById(username);

        // 如果用户不存在，则返回null
        if (user == null) return null;

        // 删除redis中的用户名
        redis.opsForValue().getOperations().delete(String.valueOf(username));
        // 创建新的Jwt令牌
        String newToken = createJwt(user);

        // 返回新的Jwt令牌
        return newToken;
    }


    /**
     * 删除指定用户的JWT token
     *
     * @param username 要删除的用户
     */
    public void deleteJwtByUsername(String username){
        redis.opsForValue().getOperations().delete(username);
    }

    /**
     * 根据用户名获取JWT
     *
     * @param username 用户名
     * @return JWT字符串
     */
    public String getJwtByUsername(String username) {
        return redis.opsForValue().get(username);
    }
}
