package com.rashmita.accuralsservice.service.AccuralsServiceImpl;
import com.rashmita.accuralsservice.service.AccuralsService;
import com.rashmita.commoncommon.entity.*;
import com.rashmita.commoncommon.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Transactional
    @Override
    public void saveInterest(String loanNumber, Long emiId, LocalDate today, double dailyInterest, int emiMonth) {
        EmiInterest emiInterest = emiInterestRepository
                .findByLoanNumberAndAccrualDateAndEmiMonth(loanNumber, today, emiMonth)
                .orElseGet(() -> {
                    EmiInterest e = new EmiInterest();
                    e.setLoanNumber(loanNumber);
                    e.setAccrualDate(today);
                    e.setEmiMonth(emiMonth);
                    return e;
                });
        emiInterest.setInterestAmount(dailyInterest);
        emiInterest.setEmiId(emiId);
        emiInterestRepository.saveAndFlush(emiInterest);
        log.info("Interest recorded for loan {} (EMI {}) on {}: {}", loanNumber, emiId, today, dailyInterest);
    }


    @Override
    public void savePenalty(String loanNumber, Long emiId, LocalDate today, double penaltyInterest, int emiMonth) {
        EmiPenalty emiPenalty=emiPenaltyRepository.findByLoanNumberAndAccrualDateAndEmiMonth(loanNumber,today,emiMonth)
                .orElse(new EmiPenalty());
        emiPenalty.setAccrualDate(today);
        emiPenalty.setPenaltyAmount(penaltyInterest);
        emiPenalty.setLoanNumber(loanNumber);
        emiPenalty.setEmiId(emiId);
        emiPenalty.setEmiMonth(emiMonth);
        emiPenaltyRepository.save(emiPenalty);
        log.info("Penalty recorded for loan {} (EMI {}) on {}: {}", loanNumber, emiId, today, penaltyInterest);
    }

    @Override
    public void saveLateFee(String loanNumber, Long emiId, LocalDate today, double lateFee, int emiMonth) {
        EmiLateFee emiLateFee=emiLateFeeRepository.findByLoanNumberAndAccrualDateAndEmiMonth(loanNumber,today,emiMonth)
                .orElse(new EmiLateFee());
        emiLateFee.setLateFee(lateFee);
        emiLateFee.setEmiId(emiId);
        emiLateFee.setAccrualDate(today);
        emiLateFee.setLoanNumber(loanNumber);
        emiLateFee.setEmiMonth(emiMonth);
        emiLateFeeRepository.save(emiLateFee);
        log.info("Late fee recorded for loan {} (EMI {}) on {}: {}", loanNumber, emiId, today, lateFee);
    }

    @Override
    public void saveOverDue(String loanNumber, Long emiId, LocalDate today, double overdueInterest, int emiMonth) {
        EmiOverdue emiOverdue=emiOverdueRepository.findByLoanNumberAndAccrualDateAndEmiMonth(loanNumber,today,emiMonth)
                .orElse(new EmiOverdue());
        emiOverdue.setAccrualDate(today);
        emiOverdue.setLoanNumber(loanNumber);
        emiOverdue.setEmiId(emiId);
        emiOverdue.setOverdueAmount(overdueInterest);
        emiOverdue.setEmiMonth(emiMonth);
        emiOverdueRepository.save(emiOverdue);
        log.info("Overdue recorded for loan {} (EMI {}) on {}: {}", loanNumber, emiId, today, overdueInterest);
    }

    @Override
    public boolean existsInterest(String loanNumber, LocalDate date, int emiId) {
        return emiInterestRepository.existsByLoanNumberAndAccrualDateAndEmiMonth(loanNumber, date, emiId);
    }

    @Override
    public boolean existsOverdue(String loanNumber, LocalDate date, int emiId) {
        return emiOverdueRepository.existsByLoanNumberAndAccrualDateAndEmiMonth(loanNumber, date, emiId);
    }

    @Override
    public boolean existsLateFee(String loanNumber, LocalDate date, int emiId) {
        return emiLateFeeRepository.existsByLoanNumberAndAccrualDateAndEmiMonth(loanNumber, date, emiId);
    }

    @Override
    public boolean existsPenalty(String loanNumber, LocalDate date, int emiId) {
        return emiPenaltyRepository.existsByLoanNumberAndAccrualDateAndEmiMonth(loanNumber, date, emiId);
    }

    @Override
    public LocalDate getLastAccrualDate(String loanNumber, Long emiId) {
        LocalDate lastInterest = Optional.ofNullable(
                        emiInterestRepository.findTopByLoanNumberAndEmiIdOrderByAccrualDateDesc(loanNumber, emiId))
                .map(EmiInterest::getAccrualDate).orElse(null);

        LocalDate lastOverdue = Optional.ofNullable(
                        emiOverdueRepository.findTopByLoanNumberAndEmiIdOrderByAccrualDateDesc(loanNumber, emiId))
                .map(EmiOverdue::getAccrualDate).orElse(null);

        LocalDate lastLateFee = Optional.ofNullable(
                        emiLateFeeRepository.findTopByLoanNumberAndEmiIdOrderByAccrualDateDesc(loanNumber, emiId))
                .map(EmiLateFee::getAccrualDate).orElse(null);

        LocalDate lastPenalty = Optional.ofNullable(
                        emiPenaltyRepository.findTopByLoanNumberAndEmiIdOrderByAccrualDateDesc(loanNumber, emiId))
                .map(EmiPenalty::getAccrualDate).orElse(null);

        return Stream.of(lastInterest, lastOverdue, lastLateFee, lastPenalty)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

}
