package com.rashmita.accuralsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.rashmita")
@EnableFeignClients
@EnableScheduling
public class AccuralsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccuralsServiceApplication.class, args);
    }

}
