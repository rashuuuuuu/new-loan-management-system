package com.rashmita.accuralsservice.service.AccuralsServiceImpl;

import com.rashmita.accuralsservice.service.AccuralsService;
import com.rashmita.commoncommon.entity.EmiInterest;
import com.rashmita.commoncommon.entity.EmiLateFee;
import com.rashmita.commoncommon.entity.EmiOverdue;
import com.rashmita.commoncommon.entity.EmiPenalty;
import com.rashmita.commoncommon.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccuralsServiceImpl implements AccuralsService {
    private final EmiInterestRepository emiInterestRepository;
    private final EmiPenaltyRepository emiPenaltyRepository;
    private final EmiOverdueRepository emiOverdueRepository;
    private final EmiLateFeeRepository emiLateFeeRepository;

    public AccuralsServiceImpl(EmiInterestRepository emiInterestRepository, EmiPenaltyRepository emiPenaltyRepository, EmiOverdueRepository emiOverdueRepository, EmiLateFeeRepository emiLateFeeRepository) {
        this.emiInterestRepository = emiInterestRepository;
        this.emiPenaltyRepository = emiPenaltyRepository;
        this.emiOverdueRepository = emiOverdueRepository;
        this.emiLateFeeRepository = emiLateFeeRepository;
    }

    @Override
    public void saveInterest(String loanNumber, Long emiId, LocalDate today, double dailyInterest) {
        EmiInterest emiInterest = new EmiInterest();
        emiInterest.setAccrualDate(today);
        emiInterest.setInterestAmount(dailyInterest);
        emiInterest.setLoanNumber(loanNumber);
        emiInterest.setEmiId(emiId);
        emiInterestRepository.save(emiInterest);
    }

    @Override
    public void savePenalty(String loanNumber, Long emiId, LocalDate today, double penaltyInterest) {
        EmiPenalty emiPenalty = new EmiPenalty();
        emiPenalty.setAccrualDate(today);
        emiPenalty.setPenaltyAmount(penaltyInterest);
        emiPenalty.setLoanNumber(loanNumber);
        emiPenalty.setEmiId(emiId);
        emiPenaltyRepository.save(emiPenalty);

    }

    @Override
    public void saveLateFee(String loanNumber, Long emiId, LocalDate today, double lateFee) {
        EmiLateFee emiLateFee = new EmiLateFee();
        emiLateFee.setLateFee(lateFee);
        emiLateFee.setEmiId(emiId);
        emiLateFee.setChargedDate(today);
        emiLateFee.setLoanNumber(loanNumber);
        emiLateFeeRepository.save(emiLateFee);

    }

    @Override
    public void saveOverDue(String loanNumber, Long emiId, LocalDate today, double overdueInterest) {
        EmiOverdue emiOverdue = new EmiOverdue();
        emiOverdue.setAccrualDate(today);
        emiOverdue.setLoanNumber(loanNumber);
        emiOverdue.setEmiId(emiId);
        emiOverdue.setOverdueAmount(overdueInterest);
        emiOverdueRepository.save(emiOverdue);
    }
}
