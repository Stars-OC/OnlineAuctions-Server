package com.onlineauctions.onlineauctions;

import com.onlineauctions.onlineauctions.utils.AesUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnlineAuctionsApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(AesUtil.DEFAULT_PASSWORD);
    }

}
