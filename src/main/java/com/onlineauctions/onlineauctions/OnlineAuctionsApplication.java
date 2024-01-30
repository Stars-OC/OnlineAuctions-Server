package com.onlineauctions.onlineauctions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.thciwei.x.file.storage.spring.EnableFileStorage;

@SpringBootApplication
@EnableFileStorage
@ServletComponentScan
@EnableAspectJAutoProxy
public class OnlineAuctionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineAuctionsApplication.class, args);
    }

}
