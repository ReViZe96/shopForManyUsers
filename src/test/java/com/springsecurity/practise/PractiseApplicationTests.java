package com.springsecurity.practise;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public
class PractiseApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testConfigureGlobal() throws Exception {
        System.out.println(new BCryptPasswordEncoder().encode("7S3d84Z9b"));
        System.out.println(new BCryptPasswordEncoder().encode("12345"));
    }
}