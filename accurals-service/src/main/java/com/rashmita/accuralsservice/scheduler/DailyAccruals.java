package com.rashmita.accuralsservice.scheduler;

import com.rashmita.accuralsservice.service.AccuralsService;
import com.rashmita.accuralsservice.service.AccuralsServiceImpl.TotalPayableImpl;
import com.rashmita.accuralsservice.service.PrepaymentService;
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
    private static final int DAYS_IN_YEAR = 365;
    private static final String JOB_NAME = "accruals_calculation";
    private final TotalPayableRepository totalPayableRepository;
    private final TotalPayableImpl totalPayableServiceImpl;
    private final PrepaymentService prepaymentService;


    @Scheduled(fixedRate = 60000) // for testing (every minute)
//     @Scheduled(cron = "0 27 11 * * ?", zone = "Asia/Kathmandu") // 3 AM daily
    public void run() {
        LocalDate today = LocalDate.now();
        log.info("Daily accrual job triggered at {}", today);

//        if (dailyJobLogRepo.existsByJobNameAndDate(JOB_NAME, today)) {
//            log.info("Daily accrual job already triggered for {}", today);
//            return;
//        } else {
        List<LoanDetails> loans = loanRepo.findByStatusIn(List.of("ACTIVE", "OVERDUE", "DEFAULT"));

        for (LoanDetails loan : loans) {
            List<EmiSchedule> schedules = emiRepo.findEmiScheduleByLoanNumber(loan.getLoanNumber());

            for (EmiSchedule s : schedules) {
                if (Boolean.TRUE.equals(s.getLastInstallment())) continue;

                if ("PENDING".equalsIgnoreCase(s.getStatus())) {
                    s.setStatus("ACTIVE");
                    emiRepo.save(s);
                }
                LocalDate accrualDate = s.getEmiStartDate();
                LocalDate lastAccrual = accuralsService.getLastAccrualDate(loan.getLoanNumber(), s.getId());
                if (lastAccrual != null) {
                    accrualDate = lastAccrual.plusDays(1);
                }
                while (!accrualDate.isAfter(today)) {
                    double remainingPrincipal = s.getRemainingAmount();
                    double dailyInterest = (remainingPrincipal * loan.getInterestRate()) / (100 * DAYS_IN_YEAR);
                    dailyInterest = round(dailyInterest);

                    if (!accuralsService.existsInterest(loan.getLoanNumber(), accrualDate, s.getId().intValue())) {
                        accuralsService.saveInterest(loan.getLoanNumber(), s.getId(), accrualDate, dailyInterest, s.getEmiMonth());
                    }

                    double paidAmount = (s.getPaidAmount() == null) ? 0.0 : s.getPaidAmount();
                    double unpaidAmount = loan.getLoanAmount() - paidAmount;
                    LocalDate overdueStart = s.getEmiDate().plusDays(1);
                    if (unpaidAmount > 0 && !accrualDate.isBefore(overdueStart)) {
                        if (!accuralsService.existsOverdue(loan.getLoanNumber(), accrualDate, s.getId().intValue())) {
                            double dailyOverdue = (unpaidAmount * loan.getOverdueInterest()) / (100 * DAYS_IN_YEAR);
                            dailyOverdue = round(dailyOverdue);
                            accuralsService.saveOverDue(loan.getLoanNumber(), s.getId(), accrualDate, dailyOverdue, s.getEmiMonth());
                        }
                        LocalDate lateFeeStart = s.getEmiDate().plusDays(1);
                        if (!accrualDate.isBefore(lateFeeStart)) {
                            if (!accuralsService.existsLateFee(loan.getLoanNumber(), accrualDate, s.getId().intValue())) {
                                accuralsService.saveLateFee(loan.getLoanNumber(), s.getId(), accrualDate, loan.getLateFeeCharge(), s.getEmiMonth());
                            }

                            if (!accuralsService.existsPenalty(loan.getLoanNumber(), accrualDate, s.getId().intValue())) {
                                double dailyPenalty = (unpaidAmount * loan.getPenaltyInterest()) / (100 * DAYS_IN_YEAR);
                                dailyPenalty = round(dailyPenalty);
                                accuralsService.savePenalty(loan.getLoanNumber(), s.getId(), accrualDate, dailyPenalty, s.getEmiMonth());
                            }
                        }
                        if (accrualDate.isAfter(s.getEmiDate().plusDays(loan.getDefaultingPeriod()))) {
                            s.setStatus("DEFAULT");
                            loan.setStatus("DEFAULT");
                        } else if (accrualDate.isAfter(s.getEmiDate())) {
                            s.setStatus("OVERDUE");
                            loan.setStatus("OVERDUE");
                        }
                    }
                    accrualDate = accrualDate.plusDays(1);
                }
                emiRepo.save(s);
                loanRepo.save(loan);

                totalPayableServiceImpl.totalPayablePerMonth(loan.getLoanNumber());
            }
        }
//            DailyJobLog dailyJobLog = new DailyJobLog();
//            dailyJobLog.setJobName(JOB_NAME);
//            dailyJobLog.setDate(today);
//            dailyJobLogRepo.save(dailyJobLog);
//    }

    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
