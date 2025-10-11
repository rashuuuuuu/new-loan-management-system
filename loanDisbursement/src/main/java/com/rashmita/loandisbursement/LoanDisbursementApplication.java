package com.rashmita.loandisbursement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoanDisbursementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanDisbursementApplication.class, args);
    }

}
