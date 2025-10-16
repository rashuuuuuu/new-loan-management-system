package com.rashmita.isoservice.service.impl;

import com.rashmita.commoncommon.model.TransactionRequest;
import com.rashmita.isoservice.entity.BankMoney;
import com.rashmita.isoservice.entity.TransactionDetail;
import com.rashmita.isoservice.repository.BankMoneyRepository;
import com.rashmita.isoservice.repository.TransactionDetailRepository;
import com.rashmita.isoservice.service.BankIsoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankIsoServiceImpl implements BankIsoService {

    private final TransactionDetailRepository transactionDetailRepository;
    private final BankMoneyRepository bankMoneyRepository;

    @Override
    @Transactional
    public void processMultiTransaction(TransactionRequest request) {
        // Source account (could come from request too)
        String fromAccount = "ACC10001";

        // Fetch source bank account
        BankMoney bankAccount = bankMoneyRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found: " + fromAccount));

        // Prepare to process
        BigDecimal totalTransfer = BigDecimal.ZERO;
        List<TransactionDetail> transactionDetails = new ArrayList<>();

        // Build transaction records
        for (var t : request.getTransactions()) {
            BigDecimal amount = BigDecimal.valueOf(t.getAmount());
            totalTransfer = totalTransfer.add(amount);

            TransactionDetail detail = new TransactionDetail();
            detail.setLoanNumber(request.getLoanNumber());
            detail.setTransactionId(request.getTransactionId());
            detail.setFromAccount(fromAccount);
            detail.setAccountNumber(t.getAccountNumber());
            detail.setTransferAmount(amount);
            detail.setParticularRemarks(t.getParticularRemarks());
            detail.setValueDate(t.getValueDate());
            detail.setTotalAmount(amount);
            transactionDetails.add(detail);
        }

        // Validate sufficient balance
        BigDecimal currentBalance = bankAccount.getTotalBalance();
        if (currentBalance.compareTo(totalTransfer) < 0) {
            throw new IllegalStateException("Insufficient balance in account: " + fromAccount);
        }

        bankAccount.setTransferAmount(totalTransfer);
        bankAccount.setTotalBalance(currentBalance.subtract(totalTransfer));
        bankMoneyRepository.save(bankAccount);
        transactionDetailRepository.saveAll(transactionDetails);

        log.info("✅ Successfully processed {} transactions. Total transferred: {} from account {}",
                transactionDetails.size(), totalTransfer, fromAccount);
    }
}
