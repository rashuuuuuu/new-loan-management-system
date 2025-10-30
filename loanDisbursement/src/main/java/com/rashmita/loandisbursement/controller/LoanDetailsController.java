package com.rashmita.loandisbursement.controller;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.service.LoanDetailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/loan")
@AllArgsConstructor
public class LoanDetailsController {
    private LoanDetailService loanDetailService;
    @GetMapping("/details/list")
    ServerResponse<?> getAllLoanDetails(){
        return loanDetailService.getAllLoanDetails();
    }
}
