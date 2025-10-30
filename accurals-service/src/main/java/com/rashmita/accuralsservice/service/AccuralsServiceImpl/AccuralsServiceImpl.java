package com.rashmita.accuralsservice.service.AccuralsServiceImpl;

import com.rashmita.accuralsservice.service.AccuralsService;
import com.rashmita.commoncommon.entity.EmiInterest;
import com.rashmita.commoncommon.entity.EmiLateFee;
import com.rashmita.commoncommon.entity.EmiOverdue;
import com.rashmita.commoncommon.entity.EmiPenalty;
import com.rashmita.commoncommon.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class AccuralsServiceImpl implements AccuralsService {

    private final EmiInterestRepository emiInterestRepository;
    private final EmiPenaltyRepository emiPenaltyRepository;
    private final EmiOverdueRepository emiOverdueRepository;
    private final EmiLateFeeRepository emiLateFeeRepository;

    public AccuralsServiceImpl(
            EmiInterestRepository emiInterestRepository,
            EmiPenaltyRepository emiPenaltyRepository,
            EmiOverdueRepository emiOverdueRepository,
            EmiLateFeeRepository emiLateFeeRepository
    ) {
        this.emiInterestRepository = emiInterestRepository;
        this.emiPenaltyRepository = emiPenaltyRepository;
        this.emiOverdueRepository = emiOverdueRepository;
        this.emiLateFeeRepository = emiLateFeeRepository;
    }

    @Override
    public void saveInterest(String loanNumber, Long emiId, LocalDate today, double dailyInterest, int emiMonth) {
        boolean exists = emiInterestRepository.existsByLoanNumberAndAccrualDate(loanNumber, today);
        if (exists) {
            log.info("Interest already recorded for loan {} on {}, skipping insert.", loanNumber, today);
            return;
        }

        EmiInterest emiInterest = new EmiInterest();
        emiInterest.setAccrualDate(today);
        emiInterest.setInterestAmount(dailyInterest);
        emiInterest.setLoanNumber(loanNumber);
        emiInterest.setEmiId(emiId);
        emiInterest.setEmiMonth(emiMonth);
        emiInterestRepository.save(emiInterest);
        log.info("Interest recorded for loan {} on {}: {}", loanNumber, today, dailyInterest);
    }

    @Override
    public void savePenalty(String loanNumber, Long emiId, LocalDate today, double penaltyInterest, int emiMonth) {
        boolean exists = emiPenaltyRepository.existsByLoanNumberAndAccrualDate(loanNumber, today);
        if (exists) {
            log.info("Penalty already recorded for loan {} on {}, skipping insert.", loanNumber, today);
            return;
        }

        EmiPenalty emiPenalty = new EmiPenalty();
        emiPenalty.setAccrualDate(today);
        emiPenalty.setPenaltyAmount(penaltyInterest);
        emiPenalty.setLoanNumber(loanNumber);
        emiPenalty.setEmiId(emiId);
        emiPenalty.setEmiMonth(emiMonth);
        emiPenaltyRepository.save(emiPenalty);
        log.info("Penalty recorded for loan {} on {}: {}", loanNumber, today, penaltyInterest);
    }

    @Override
    public void saveLateFee(String loanNumber, Long emiId, LocalDate today, double lateFee, int emiMonth) {
        boolean exists = emiLateFeeRepository.existsByLoanNumberAndAccrualDate(loanNumber, today);
        if (exists) {
            log.info("Late fee already recorded for loan {} on {}, skipping insert.", loanNumber, today);
            return;
        }

        EmiLateFee emiLateFee = new EmiLateFee();
        emiLateFee.setLateFee(lateFee);
        emiLateFee.setEmiId(emiId);
        emiLateFee.setAccrualDate(today);
        emiLateFee.setLoanNumber(loanNumber);
        emiLateFee.setEmiMonth(emiMonth);
        emiLateFeeRepository.save(emiLateFee);
        log.info("Late fee recorded for loan {} on {}: {}", loanNumber, today, lateFee);
    }

    @Override
    public void saveOverDue(String loanNumber, Long emiId, LocalDate today, double overdueInterest, int emiMonth) {
        boolean exists = emiOverdueRepository.existsByLoanNumberAndAccrualDate(loanNumber, today);
        if (exists) {
            log.info("Overdue already recorded for loan {} on {}, skipping insert.", loanNumber, today);
            return;
        }

        EmiOverdue emiOverdue = new EmiOverdue();
        emiOverdue.setAccrualDate(today);
        emiOverdue.setLoanNumber(loanNumber);
        emiOverdue.setEmiId(emiId);
        emiOverdue.setOverdueAmount(overdueInterest);
        emiOverdue.setEmiMonth(emiMonth);
        emiOverdueRepository.save(emiOverdue);
        log.info("Overdue recorded for loan {} on {}: {}", loanNumber, today, overdueInterest);
    }
}
