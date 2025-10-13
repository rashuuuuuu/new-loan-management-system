package com.rashmita.isoservice.controller;
import com.rashmita.commoncommon.model.TransactionRequest;
import com.rashmita.isoservice.service.BankIsoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/bank-iso")
public class BankIsoController {

    private final BankIsoService bankIsoService;

    public BankIsoController(BankIsoService bankIsoService) {
        this.bankIsoService = bankIsoService;
    }
    @PostMapping("/transfer")
    public ResponseEntity<String> processMultiTransaction(@RequestBody TransactionRequest request) {
        bankIsoService.processMultiTransaction(request);
        return ResponseEntity.ok("Transaction processed successfully");
    }
}