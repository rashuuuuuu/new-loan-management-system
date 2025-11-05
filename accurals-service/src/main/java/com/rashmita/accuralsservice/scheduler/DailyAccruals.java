package com.rashmita.accuralsservice.scheduler;
import com.rashmita.accuralsservice.service.AccuralsService;
import com.rashmita.accuralsservice.service.AccuralsServiceImpl.TotalPayableImpl;
import com.rashmita.accuralsservice.service.TotalPayable;
import com.rashmita.commoncommon.entity.EmiSchedule;
import com.rashmita.commoncommon.entity.LoanDetails;
import com.rashmita.commoncommon.repository.DailyJobLogRepository;
import com.rashmita.commoncommon.repository.EmiScheduleRepository;
import com.rashmita.commoncommon.repository.LoanDetailsRepository;
import com.rashmita.commoncommon.repository.TotalPayableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class DailyAccruals {
    private final LoanDetailsRepository loanRepo;
    private final EmiScheduleRepository emiRepo;
    private final AccuralsService accuralsService;
    private final DailyJobLogRepository dailyJobLogRepo;
    private final TotalPayable totalPayableService;
    private final TotalPayableRepository totalPayableRepository;
    private final TotalPayableImpl totalPayableServiceImpl;
    private static final int DAYS_IN_YEAR = 365;
    private static final String JOB_NAME = "accruals_calculation";
    @Scheduled(fixedRate = 60000) // every minute for testing
//  @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Kathmandu") // 3 AM daily (production)
    public void run() {
        LocalDate today = LocalDate.now();
        log.info("Daily accrual job triggered at {}", today);
        List<LoanDetails> loans = loanRepo.findByStatusIn(List.of("ACTIVE", "OVERDUE", "DEFAULT"));
        for (LoanDetails loan : loans) {
            List<EmiSchedule> schedules = emiRepo.findEmiScheduleByLoanNumber(loan.getLoanNumber());
            for (EmiSchedule s : schedules) {
                if (Boolean.TRUE.equals(s.getLastInstallment())) continue;
                if ("PENDING".equalsIgnoreCase(s.getStatus())
                        && (today.isEqual(s.getEmiStartDate()) || today.isAfter(s.getEmiStartDate()))
                        && (today.isBefore(s.getEmiDate()) || today.isEqual(s.getEmiDate()))) {
                    s.setStatus("ACTIVE");
                    emiRepo.save(s);
                }
                LocalDate accrualDate = accuralsService.getLastAccrualDate(loan.getLoanNumber(), s.getId());
                accrualDate = (accrualDate == null) ? s.getEmiStartDate() : accrualDate.plusDays(1);
                while (!accrualDate.isAfter(today)) {
                    double remainingPrincipal = s.getBeginningBalance();
                    double unpaidAmount = (s.getUnpaidAmount() == null) ? s.getPrincipalComponent() : s.getUnpaidAmount();
                    LocalDate emiStartDate = s.getEmiStartDate();
                    LocalDate emiEndDate = s.getEmiDate();
                    if (!accrualDate.isBefore(emiStartDate) && !accrualDate.isAfter(emiEndDate)) {
                        double dailyInterest = (remainingPrincipal * loan.getInterestRate()) / (100 * DAYS_IN_YEAR);
                        dailyInterest = round(dailyInterest);
                        if (!accuralsService.existsInterest(loan.getLoanNumber(), accrualDate, s.getEmiMonth())) {
                            accuralsService.saveInterest(
                                    loan.getLoanNumber(),
                                    s.getId(),
                                    accrualDate,
                                    dailyInterest,
                                    s.getEmiMonth()
                            );
                        }
                        double dailyOverdue = (unpaidAmount * loan.getOverdueInterest()) / (100 * DAYS_IN_YEAR);
                        dailyOverdue = round(dailyOverdue);
                        if (!accuralsService.existsOverdue(loan.getLoanNumber(), accrualDate, s.getEmiMonth())) {
                            accuralsService.saveOverDue(
                                    loan.getLoanNumber(),
                                    s.getId(),
                                    accrualDate,
                                    dailyOverdue,
                                    s.getEmiMonth()
                            );
                        }
                        if (!accuralsService.existsLateFee(loan.getLoanNumber(), accrualDate, s.getEmiMonth())) {
                            accuralsService.saveLateFee(
                                    loan.getLoanNumber(),
                                    s.getId(),
                                    accrualDate,
                                    loan.getLateFeeCharge(),
                                    s.getEmiMonth()
                            );
                        }
                        double dailyPenalty = (unpaidAmount * loan.getPenaltyInterest()) / (100 * DAYS_IN_YEAR);
                        dailyPenalty = round(dailyPenalty);
                        if (!accuralsService.existsPenalty(loan.getLoanNumber(), accrualDate, s.getEmiMonth())) {
                            accuralsService.savePenalty(
                                    loan.getLoanNumber(),
                                    s.getId(),
                                    accrualDate,
                                    dailyPenalty,
                                    s.getEmiMonth()
                            );
                        }
                    }
                    if (accrualDate.isAfter(s.getEmiDate().plusDays(loan.getDefaultingPeriod()))) {
                        s.setStatus("DEFAULT");
                        loan.setStatus("DEFAULT");
                    } else if (accrualDate.isAfter(s.getEmiDate())) {
                        s.setStatus("OVERDUE");
                        loan.setStatus("OVERDUE");
                    }
                    accrualDate = accrualDate.plusDays(1);
                }
                emiRepo.save(s);
                loanRepo.save(loan);
                totalPayableServiceImpl.totalPayablePerMonth(loan.getLoanNumber());
            }
        }
        log.info(" Daily accrual job completed successfully at {}", today);
    }
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
