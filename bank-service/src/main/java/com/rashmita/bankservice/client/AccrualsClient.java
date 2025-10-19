package com.rashmita.bankservice.client;
import com.rashmita.bankservice.mapper.LoanReportDto;
import com.rashmita.bankservice.model.LoanNumberModel;
import com.rashmita.common.model.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "accurals-service", url = "http://localhost:9091")
public interface AccrualsClient {
    @PostMapping("/accruals/by/loannumber")
    ServerResponse<?> createTotalAccrualsByLoanNumber(@RequestBody LoanNumberModel loanNumber) ;

    @GetMapping("accruals/report")
    List<LoanReportDto> getTotalAccrualsByLoanNumber();


    }
