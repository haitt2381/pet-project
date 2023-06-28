package com.pp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pp.common", "com.pp.auth"})
public class PpAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PpAuthApplication.class, args);
    }

}
