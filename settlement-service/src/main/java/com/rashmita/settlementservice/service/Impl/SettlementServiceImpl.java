package com.rashmita.settlementservice.service.Impl;

import com.rashmita.commoncommon.entity.TotalPayableEntity;
import com.rashmita.commoncommon.model.SettlementRequest;
import com.rashmita.commoncommon.repository.TotalPayableRepository;
import com.rashmita.settlementservice.client.IsoClient;
import com.rashmita.settlementservice.service.SettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SettlementServiceImpl implements SettlementService {
    private final IsoClient isoClient;
    private final TotalPayableRepository totalPayableRepository;

    public SettlementServiceImpl(IsoClient isoClient, TotalPayableRepository totalPayableRepository) {
        this.isoClient = isoClient;
        this.totalPayableRepository = totalPayableRepository;
    }

    @Override
    public void createSettlement(SettlementRequest request) {
        SettlementRequest settlementRequest = new SettlementRequest();
        settlementRequest.setLoanNumber(request.getLoanNumber());
        settlementRequest.setTransactionId(request.getTransactionId());
        settlementRequest.setTransactionId(request.getTransactionId());
        settlementRequest.setAccountNumber(request.getAccountNumber());
        settlementRequest.setAmount(request.getAmount());
        settlementRequest.setEmiMonth(request.getEmiMonth());
        settlementRequest.setTransactions(request.getTransactions());
        isoClient.isoSettlement(settlementRequest);
    }
    public double calculateTotalLateFeeByUnPaidStatus(String loanNumber){
        List<TotalPayableEntity> totalPayableEntities=totalPayableRepository.findByLoanNumber(loanNumber);
        return totalPayableEntities.stream()
                .filter(totalPayableEntity -> totalPayableEntity.getStatus().equals("UNPAID"))
                .mapToDouble(TotalPayableEntity::getPayableLateFee)
                .sum();
    }
    public double calculateTotalInterestByUnpaidStatus(String loanNumber){
        List<TotalPayableEntity> totalPayableEntities=totalPayableRepository.findByLoanNumber(loanNumber);
        return totalPayableEntities.stream()
                .filter(totalPayableEntity -> totalPayableEntity.getStatus().equals("UNPAID"))
                .mapToDouble(TotalPayableEntity::getPayableInterest)
                .sum();
    }
    public double calculateTotalOverdueByLoanNumber(String loanNumber){
        List<TotalPayableEntity> totalPayableEntities=totalPayableRepository.findByLoanNumber(loanNumber);
        return totalPayableEntities.stream()
                .filter(totalPayableEntity -> totalPayableEntity.getStatus().equals("UNPAID"))
                .mapToDouble(TotalPayableEntity::getPayableOverdue)
                .sum();
    }

    public double calculateTotalPenaltyByLoanNumber(String loanNumber){
        List<TotalPayableEntity> totalPayableEntities=totalPayableRepository.findByLoanNumber(loanNumber);
        return totalPayableEntities.stream()
                .filter(totalPayableEntity -> totalPayableEntity.getStatus().equals("UNPAID"))
                .mapToDouble(TotalPayableEntity::getPayablePenalty)
                .sum();
    }
    public double calculateTotalPrincipalByLoanNumber(String loanNumber){
        List<TotalPayableEntity> totalPayableEntities=totalPayableRepository.findByLoanNumber(loanNumber);
        return totalPayableEntities.stream()
                .filter(totalPayableEntity -> totalPayableEntity.getStatus().equals("UNPAID"))
                .mapToDouble(TotalPayableEntity::getPayablePrincipal)
                .sum();
    }

}
