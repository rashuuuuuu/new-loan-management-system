package com.rashmita.bankservice.client;
import com.rashmita.bankservice.model.LoanNumberModel;
import com.rashmita.common.model.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "accurals-service", url = "http://localhost:9091")
public interface AccrualsClient {
    @PostMapping("/accruals/by/loannumber")
    public ServerResponse<?> createTotalAccrualsByLoanNumber(@RequestBody LoanNumberModel loanNumber) ;

    }
