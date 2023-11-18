package com.cloud.fish.identification;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@MapperScan("com.cloud.fish.identification.mapper")
@ComponentScan(basePackages = "com.cloud.fish.identification")
public class IdentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentificationApplication.class, args);
    }

}
