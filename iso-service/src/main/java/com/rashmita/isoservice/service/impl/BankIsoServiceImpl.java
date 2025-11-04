package com.rashmita.isoservice.service.impl;

import com.rashmita.commoncommon.entity.AmountUpdateRequest;
import com.rashmita.commoncommon.model.SettlementRequest;
import com.rashmita.commoncommon.model.TransactionRequest;
import com.rashmita.isoservice.client.BankClient;
import com.rashmita.isoservice.entity.BankMoney;
import com.rashmita.isoservice.entity.SettlementDetail;
import com.rashmita.isoservice.entity.TransactionDetail;
import com.rashmita.isoservice.repository.BankMoneyRepository;
import com.rashmita.isoservice.repository.SettlementDetailRepository;
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
    private final SettlementDetailRepository settlementDetailRepository;
    private final BankClient bankClient;

    @Override
    @Transactional
    public void processMultiTransaction(TransactionRequest request) {
        String fromAccount = "ACC10001";
        BankMoney bankAccount = bankMoneyRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found: " + fromAccount));
        BigDecimal totalTransfer = BigDecimal.ZERO;
        List<TransactionDetail> transactionDetails = new ArrayList<>();
        for (var t : request.getTransactions()) {
            BigDecimal amount = BigDecimal.valueOf(t.getAmount());
            totalTransfer = totalTransfer.add(amount);
            TransactionDetail detail = new TransactionDetail();
            detail.setLoanNumber(request.getLoanNumber());
            detail.setTransactionId(request.getTransactionId());
            detail.setFromAccount(fromAccount);
            detail.setAccountNumber(t.getAccountNumber());
            detail.setType(t.getType());
            detail.setTransferAmount(amount);
            detail.setParticularRemarks(t.getParticularRemarks());
            detail.setValueDate(t.getValueDate());
            detail.setTotalAmount(amount);
            detail.setStatus("Success");
            transactionDetails.add(detail);
        }
        BigDecimal currentBalance = bankAccount.getTotalBalance();
        if (currentBalance.compareTo(totalTransfer) < 0) {
            throw new IllegalStateException("Insufficient balance in account: " + fromAccount);
        }
        bankAccount.setTransferAmount(totalTransfer);
        bankAccount.setTotalBalance(currentBalance.subtract(totalTransfer));
        bankMoneyRepository.save(bankAccount);
        transactionDetailRepository.saveAll(transactionDetails);
        log.info(" Successfully processed {} transactions. Total transferred: {} from account {}",
                transactionDetails.size(), totalTransfer, fromAccount);
    }

    public void isoSettlement(SettlementRequest request) {
        String toAccount = "ACC10001";
        BankMoney bankAccount = bankMoneyRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found: " + toAccount));
        BigDecimal totalTransfer = BigDecimal.ZERO;
        List<SettlementDetail> settlementDetails = new ArrayList<>();
        for (var t : request.getTransactions()) {
            BigDecimal amount = BigDecimal.valueOf(t.getAmount());
            totalTransfer = totalTransfer.add(amount);
            SettlementDetail detail = new SettlementDetail();
            detail.setLoanNumber(request.getLoanNumber());
            detail.setToAccount(toAccount);
            detail.setAccountNumber(t.getAccountNumber());
            detail.setTransferAmount(amount);
            detail.setParticularRemarks(t.getParticularRemarks());
            detail.setValueDate(t.getValueDate());
            detail.setType(t.getType());
            detail.setTotalAmount(amount);
            detail.setStatus("Success");
            detail.setEmiMonth(request.getEmiMonth());
            settlementDetails.add(detail);
        }
        BigDecimal currentBalance = BigDecimal.valueOf(request.getAmount());
        if (currentBalance.compareTo(totalTransfer) < 0) {
            throw new IllegalStateException("Insufficient balance in account number: " + request.getAccountNumber());
        }
        bankAccount.setTransferAmount(totalTransfer);
        bankAccount.setTotalBalance(currentBalance.add(totalTransfer));
        String accountNumber=request.getAccountNumber();
        AmountUpdateRequest amountUpdateRequest=new AmountUpdateRequest();
        amountUpdateRequest.setAccountNumber(accountNumber);
        amountUpdateRequest.setAmount(request.getAmount());
        bankClient.updateAmountByCustomerNumber(amountUpdateRequest);
        bankMoneyRepository.save(bankAccount);
        settlementDetailRepository.saveAll(settlementDetails);
        log.info(" Successfully Settled {} transactions. Total transferred: {} from account {}",
                settlementDetails.size(), totalTransfer, request.getAccountNumber());
    }

    public void isoPrepayment(TransactionRequest request) {
        String toAccount = "ACC10001";
        BankMoney bankAccount = bankMoneyRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found: " + toAccount));
        BigDecimal totalTransfer = BigDecimal.ZERO;
        List<TransactionDetail> transactionDetails = new ArrayList<>();
        for (var t : request.getTransactions()) {
            BigDecimal amount = BigDecimal.valueOf(t.getAmount());
            totalTransfer = totalTransfer.add(amount);
            TransactionDetail detail = new TransactionDetail();
            detail.setLoanNumber(request.getLoanNumber());
            detail.setFromAccount(request.getTransactions().getFirst().getAccountNumber());
            detail.setTransactionId(request.getTransactionId());
            detail.setToAccount(toAccount);
            detail.setAccountNumber(t.getAccountNumber());
            detail.setType(t.getType());
            detail.setTransferAmount(amount);
            detail.setParticularRemarks(t.getParticularRemarks());
            detail.setValueDate(t.getValueDate());
            detail.setTotalAmount(amount);
            detail.setStatus("Success");
            transactionDetails.add(detail);
        }
        BigDecimal currentBalance = BigDecimal.valueOf(request.getTransactions().get(0).getAmount());
        if (currentBalance.compareTo(totalTransfer) < 0) {
            throw new IllegalStateException("Insufficient balance in account number: " + request.getTransactions().get(0).getAccountNumber());
        }
        bankAccount.setTransferAmount(totalTransfer);
        bankAccount.setTotalBalance(currentBalance.add(totalTransfer));
        bankMoneyRepository.save(bankAccount);
        transactionDetailRepository.saveAll(transactionDetails);
        log.info(" Successfully Settled {} transactions. Total transferred: {} ",
                transactionDetails.size(), totalTransfer);
    }
}

