package com.cloud.fish.thirdparty;

import com.cloud.fish.thirdparty.service.CountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThirdPartyApplicationTests {
    @Autowired
    CountService countService;
    @Test
    void contextLoads() {

    }

}
