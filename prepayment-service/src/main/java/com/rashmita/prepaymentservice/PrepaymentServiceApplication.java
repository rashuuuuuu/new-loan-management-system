package com.rashmita.prepaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication(scanBasePackages = "com.rashmita")
@EnableFeignClients
public class PrepaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrepaymentServiceApplication.class, args);
    }

}
