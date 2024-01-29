package com.onlineauctions.onlineauctions.pojo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UsernameAndPWD {

    @NotNull(message = "用户名不能为空")
    private Long username;

    @NotEmpty(message = "密码不能为空")
    private String password;

}
