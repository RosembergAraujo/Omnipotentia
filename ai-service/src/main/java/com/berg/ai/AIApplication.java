package com.berg.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.berg.ai", "com.berg.common"})
public class AIApplication {
    public static void main(String[] args) {
        SpringApplication.run(AIApplication.class, args);
    }
}
