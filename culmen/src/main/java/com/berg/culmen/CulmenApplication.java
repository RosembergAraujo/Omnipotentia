package com.berg.culmen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"com.berg.culmen", "com.berg.common"})
@EnableCaching
public class CulmenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CulmenApplication.class, args);
    }
}
