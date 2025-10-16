package com.rashmita.accuralsservice.controller;
import com.rashmita.accuralsservice.service.TotalPayable;
import com.rashmita.commoncommon.constants.ApiConstants;
import com.rashmita.commoncommon.model.CreateTotalAccrual;
import com.rashmita.commoncommon.model.LoanNumberModel;
import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ACCRUALS)
@RequiredArgsConstructor
public class AccrualsController {
    private final TotalPayable totalPayable;
    @PostMapping("/by/loannumber")
    public ServerResponse<?> createTotalAccrualsByLoanNumber(@RequestBody LoanNumberModel loanNumber) {
        CreateTotalAccrual accrual = totalPayable.calculateAccrualsByLoanNumber(loanNumber);
       return ResponseUtility.getSuccessfulServerResponse(accrual,"fetched total accruals ");
    }
       }
