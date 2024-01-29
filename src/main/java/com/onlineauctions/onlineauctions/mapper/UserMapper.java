package com.onlineauctions.onlineauctions.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onlineauctions.onlineauctions.pojo.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 更新用户头像
     *
     * @param username 用户名
     * @param uploadUrl 头像地址
     */
    @Update("update user set avatar_url = #{uploadUrl} where username = #{username}")
    void updateAvatar(Long username, String uploadUrl);




}
