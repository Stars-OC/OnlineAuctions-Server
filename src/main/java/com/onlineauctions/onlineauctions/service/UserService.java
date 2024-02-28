package com.onlineauctions.onlineauctions.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onlineauctions.onlineauctions.mapper.UserMapper;
import com.onlineauctions.onlineauctions.pojo.PageList;
import com.onlineauctions.onlineauctions.pojo.user.User;
import com.onlineauctions.onlineauctions.service.redis.JwtService;
import com.onlineauctions.onlineauctions.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;





    /**
     * 更新用户信息
     *
     * @param user    要更新的用户对象
     * @return 更新结果及更新后的用户对象
     */
    public String updateInfo(User user) {
        if (user.getNewPassword() != null){
            User oldUser = userMapper.selectById(user.getUsername());
            String newPassword = AesUtil.encrypt(user.getNewPassword());
            if (oldUser.getPassword().equalsIgnoreCase(newPassword)) {
                // 对更新后的用户密码进行加密
                user.setPassword(newPassword);
            }else {
                return null;
            }
        }

        // 更新用户信息至数据库
        userMapper.updateById(user);
        String jwt = jwtService.createJwt(user);
        // 返回更新成功的提示信息及更新后的用户对象
        return jwt;
    }





    /**
     * 设置用户头像
     * @param username 用户名
     * @param thUrl 头像URL
     */
    public void setAvatar(Long username, String thUrl) {
        userMapper.updateAvatar(username, thUrl);
    }

    /**
     * 获取用户列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param filter 过滤条件
     * @return 分页列表
     */
    public PageList<User> getUserList(int pageNum, int pageSize, String filter) {
        // 创建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(filter)) queryWrapper.like("nickname", filter);
        queryWrapper.orderByAsc("create_at");
        // 创建分页对象
        Page<User> userPage = new Page<>(pageNum, pageSize);

        // 执行查询并获取分页结果
        Page<User> selectPage = userMapper.selectPage(userPage, queryWrapper);
        // 返回分页列表
        return new PageList<>(selectPage);
    }
}
