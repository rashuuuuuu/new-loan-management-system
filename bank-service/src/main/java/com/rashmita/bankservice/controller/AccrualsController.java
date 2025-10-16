package com.rashmita.bankservice.controller;
import com.rashmita.bankservice.client.AccrualsClient;
import com.rashmita.bankservice.model.LoanNumberModel;
import com.rashmita.common.constants.ApiConstants;
import com.rashmita.common.model.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ACCRUALS)
@RequiredArgsConstructor
public class AccrualsController {
    private final AccrualsClient accrualsClient;
    @PostMapping("/by/loannumber")
    public ServerResponse<?> getAccrual(@RequestBody LoanNumberModel loanNumber) {
        return accrualsClient.createTotalAccrualsByLoanNumber(loanNumber);
    }
}
