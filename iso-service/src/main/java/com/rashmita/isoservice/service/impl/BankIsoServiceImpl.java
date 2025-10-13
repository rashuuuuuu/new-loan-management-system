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
        String fromAccount = "ACC10001";
        BankMoney bankAccount = bankMoneyRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found: " + fromAccount));

        BigDecimal totalTransfer = BigDecimal.ZERO;
        List<TransactionDetail> details = new ArrayList<>();
        for (var t : request.getTransactions()) {
            BigDecimal amount = BigDecimal.valueOf(t.getAmount());
            totalTransfer = totalTransfer.add(amount);
            TransactionDetail td = new TransactionDetail();
            td.setLoanNumber(request.getLoanNumber());
            td.setTransactionId(request.getTransactionId());
            td.setFromAccount(fromAccount);
            td.setAccountNumber(t.getAccountNumber());
            td.setTransferAmount(amount);
            td.setParticularRemarks(t.getParticularRemarks());
            td.setValueDate(t.getValueDate());
            td.setTotalAmount(amount);
            details.add(td);
        }
        BigDecimal currentBalance = bankAccount.getTotalBalance();
        BigDecimal newBalance = currentBalance.subtract(totalTransfer);
        bankAccount.setTransferAmount(totalTransfer);
        bankAccount.setTotalBalance(newBalance);
        bankMoneyRepository.save(bankAccount);
        transactionDetailRepository.saveAll(details);
        log.info("Processed {} transactions, total transfer = {}", details.size(), totalTransfer);
    }
}
