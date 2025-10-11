package com.rashmita.loandisbursement.controller;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.model.LoanProcessRequest;
import com.rashmita.loandisbursement.service.LoanProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanProcess {
    private final LoanProcessService loanProcessService;
    @PostMapping("/process")
    public ServerResponse<?> processLoan(@RequestBody LoanProcessRequest loanProcessRequest) {
      return loanProcessService.loanProcess(loanProcessRequest);
    }
}