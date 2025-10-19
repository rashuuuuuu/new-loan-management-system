package com.rashmita.accuralsservice.controller;
import com.rashmita.accuralsservice.service.TotalPayable;
import com.rashmita.commoncommon.constants.ApiConstants;
import com.rashmita.commoncommon.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/report")
    public List<LoanReportDto> getAllLoanAccrualsReport(){
        return totalPayable.getAllReport();
    }
       }
