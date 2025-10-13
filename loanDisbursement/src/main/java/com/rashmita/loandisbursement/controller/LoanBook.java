package com.rashmita.loandisbursement.controller;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.loandisbursement.model.LoanBookRequest;
import com.rashmita.loandisbursement.service.LoanBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanBook {
        private final LoanBookService loanBookService;
        @PostMapping("/book")
        public ServerResponse<?> processLoan(@RequestBody LoanBookRequest loanBookRequest) {
            return loanBookService.loanBook(loanBookRequest);
        }
    }
