package com.rashmita.loandisbursement.controller;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.model.LoanConfirmRequest;
import com.rashmita.loandisbursement.service.LoanConfirmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanConfirm {
    private final LoanConfirmService loanconfirmService;
    @PostMapping("/confirm")
    public ServerResponse<?> processLoan(@RequestBody LoanConfirmRequest loanBookRequest) {
        return loanconfirmService.confirmLoan(loanBookRequest);
    }
}