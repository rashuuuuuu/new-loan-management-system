package com.rashmita.isoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IsoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsoServiceApplication.class, args);
    }

}
