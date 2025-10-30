package com.rashmita.accuralsservice.scheduler;

import com.rashmita.accuralsservice.service.AccuralsService;
import com.rashmita.commoncommon.entity.EmiSchedule;
import com.rashmita.commoncommon.entity.LoanDetails;
import com.rashmita.commoncommon.repository.EmiScheduleRepository;
import com.rashmita.commoncommon.repository.LoanDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyAccruals {

    private final LoanDetailsRepository loanRepo;
    private final EmiScheduleRepository emiRepo;
    private final AccuralsService accuralsService;
    private static final int DAYS_IN_YEAR = 365;

    @Scheduled(fixedRate = 60000) // every minute for testing
//    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Kathmandu") // 3 AM daily
    public void run() {
        LocalDate today = LocalDate.now();
        log.info("Daily accrual job triggered at {}", today);

        List<LoanDetails> loans = loanRepo.findByStatusIn(List.of("ACTIVE", "OVERDUE", "DEFAULT"));
        for (LoanDetails loan : loans) {
            List<EmiSchedule> schedules = emiRepo.findEmiScheduleByLoanNumber(loan.getLoanNumber());
            for (EmiSchedule s : schedules) {

                // Skip last installment
                if (Boolean.TRUE.equals(s.getLastInstallment())) continue;

                // Ensure schedule status is active
                if ("PENDING".equalsIgnoreCase(s.getStatus())) {
                    s.setStatus("ACTIVE");
                    emiRepo.save(s);
                }

                // Calculate daily interest
                double remainingPrincipal = s.getRemainingAmount();
                double dailyInterest = (remainingPrincipal * loan.getInterestRate()) / (100 * DAYS_IN_YEAR);
                dailyInterest = round(dailyInterest);

                accuralsService.saveInterest(loan.getLoanNumber(), s.getId(), today, dailyInterest, s.getEmiMonth());

                // Overdue logic
                if (s.getEmiDate().isBefore(today)) {
                    double paidAmount = (s.getPaidAmount() == null) ? 0.0 : s.getPaidAmount();
                    double unpaidAmount = s.getEmiAmount() - paidAmount;

                    if (unpaidAmount > 0) {
                        s.setStatus("OVERDUE");
                        loan.setStatus("OVERDUE");

                        double dailyOverdue = (unpaidAmount * loan.getOverdueInterest()) / (100 * DAYS_IN_YEAR);
                        dailyOverdue = round(dailyOverdue);
                        accuralsService.saveOverDue(loan.getLoanNumber(), s.getId(), today, dailyOverdue, s.getEmiMonth());

                        LocalDate graceDate = s.getEmiDate().plusDays(1);
                        if (today.isAfter(graceDate)) {
                            accuralsService.saveLateFee(loan.getLoanNumber(), s.getId(), today, loan.getLateFeeCharge(), s.getEmiMonth());

                            double dailyPenalty = (unpaidAmount * loan.getPenaltyInterest()) / (100 * DAYS_IN_YEAR);
                            dailyPenalty = round(dailyPenalty);
                            accuralsService.savePenalty(loan.getLoanNumber(), s.getId(), today, dailyPenalty, s.getEmiMonth());
                        }

                        // Defaulting condition
                        if (today.isAfter(s.getEmiDate().plusDays(loan.getDefaultingPeriod()))) {
                            s.setStatus("DEFAULT");
                            loan.setStatus("DEFAULT");
                        }
                        emiRepo.save(s);
                        loanRepo.save(loan);
                    }
                }
            }
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
