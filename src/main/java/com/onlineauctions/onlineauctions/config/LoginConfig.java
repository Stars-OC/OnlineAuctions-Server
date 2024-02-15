package com.onlineauctions.onlineauctions.config;

import com.onlineauctions.onlineauctions.pojo.config.NoLogin;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "user")
public class LoginConfig {
    private int verifyTime;
    private NoLogin noLogin;
}
