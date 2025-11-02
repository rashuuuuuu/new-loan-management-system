package com.rashmita.accuralsservice.controller;

import com.rashmita.accuralsservice.service.AccuralsServiceImpl.TotalPayableImpl;
import com.rashmita.commoncommon.model.CreateTotalAccrual;
import com.rashmita.commoncommon.model.LoanNumberModel;
import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.commoncommon.repository.TotalPayableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/totalPayable")
@RequiredArgsConstructor
public class TotalPayableController {
    private final TotalPayableImpl totalPayable;

    @PostMapping("accruals/by/loannumber")
    public ServerResponse<?> create(@RequestBody LoanNumberModel loanNumber) {
        return ResponseUtility.getSuccessfulServerResponse(totalPayable.createTotalAccruedEntity(loanNumber));
    }
    @PostMapping("totalPayable/by/loannumber")
    public ServerResponse<?> createTotalAccruals(@RequestBody LoanNumberModel loanNumber) {
        return ResponseUtility.getSuccessfulServerResponse(totalPayable.totalPayablePerMonth(loanNumber));
    }
}
