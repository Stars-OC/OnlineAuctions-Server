package com.onlineauctions.onlineauctions.pojo.user;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlineauctions.onlineauctions.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;


/**
 * 用户类
 */
@Data
@NoArgsConstructor
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long username;

    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    private String description;

    @URL(message = "头像地址不合法")
    private String avatarUrl;


    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT,updateStrategy = FieldStrategy.NEVER)
    private Long createAt;

    public User(String token) {
        Claims claims = JwtUtil.getClaims(token);
        this.username = claims.get("username", Long.class);
        this.nickname = claims.get("nickname", String.class);
        this.avatarUrl = claims.get("avatarUrl", String.class);
        this.createAt = claims.get("createAt", Long.class);
        this.role = claims.get("role", Integer.class);
    }
}
