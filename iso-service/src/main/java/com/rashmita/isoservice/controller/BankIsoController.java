package com.rashmita.isoservice.controller;

import com.rashmita.commoncommon.model.FundTransferRequest;
import com.rashmita.commoncommon.model.FundTransferResponse;
import com.rashmita.isoservice.service.BankIsoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank-iso")
@RequiredArgsConstructor
public class BankIsoController {

    private final BankIsoService bankIsoService;

    @PostMapping("/transfer")
    public ResponseEntity<FundTransferResponse> transferFunds(@RequestBody @Valid FundTransferRequest request) {
        FundTransferResponse response = bankIsoService.transferFunds(request);
        return ResponseEntity.ok(response);
    }
}
