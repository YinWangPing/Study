package com.yailjj.cloud.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yailjj.cloud.zero")
public class UserProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(UserProviderApp.class, args);
    }

}
