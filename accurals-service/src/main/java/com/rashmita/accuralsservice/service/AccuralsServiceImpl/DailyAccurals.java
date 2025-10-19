package com.rashmita.accuralsservice.service.AccuralsServiceImpl;

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
public class DailyAccurals {

    private final LoanDetailsRepository loanRepo;
    private final EmiScheduleRepository emiRepo;
    private final AccuralsService accuralsService;
    private final TotalPayableImpl totalPayable;

    private static final int DAYS_IN_YEAR = 365;

    @Scheduled(fixedRate = 180000)
    @Scheduled(cron = "0 10 2 * * ?", zone = "Asia/Kathmandu")
    public void run() {
        log.info("Daily accrual job triggered at {}", LocalDate.now());
        List<LoanDetails> loans = loanRepo.findByStatusIn(List.of("ACTIVE", "OVERDUE", "DEFAULT"));
        LocalDate today = LocalDate.now();

        for (LoanDetails loan : loans) {
            List<EmiSchedule> schedules = emiRepo.findEmiScheduleByLoanNumber(loan.getLoanNumber());

            for (EmiSchedule s : schedules) {
                if (Boolean.TRUE.equals(s.getLastInstallment())) continue;
                long daysDiff = ChronoUnit.DAYS.between(s.getEmiStartDate(), s.getEmiDate());
                if (daysDiff > 0) {
                    s.setStatus("ACTIVE");
                    emiRepo.save(s);
                    loanRepo.save(loan);
                }

                if (s.getEmiDate().isBefore(today)
                        && !"OVERDUE".equalsIgnoreCase(s.getStatus())) {
                    s.setStatus("OVERDUE");
                    loan.setStatus("OVERDUE");
                    emiRepo.save(s);
                    loanRepo.save(loan);
                }
                LocalDate emiStart = s.getEmiStartDate();
                if (!today.isBefore(emiStart) && !"PAID".equalsIgnoreCase(s.getStatus())) {
                    double principal = loan.getLoanAmount();
                    double annualRate = loan.getInterestRate();
                    double dailyInterest = (principal * annualRate) / (100 * DAYS_IN_YEAR);
                    dailyInterest = round(dailyInterest);
                    accuralsService.saveInterest(
                            loan.getLoanNumber(), s.getId(), today, dailyInterest, s.getEmiMonth()
                    );
                }

                // Update EMI status to ACTIVE if today >= start date
                if (!today.isBefore(emiStart) && !"PAID".equalsIgnoreCase(s.getStatus()) && !"OVERDUE".equalsIgnoreCase(s.getStatus())) {
                    s.setStatus("ACTIVE");
                    emiRepo.save(s);
                    loanRepo.save(loan);
                }
                double paidAmount = (s.getPaidAmount() == null) ? 0.0 : s.getPaidAmount();
                double unpaidAmount = s.getEmiAmount() - paidAmount;

                if (s.getEmiDate().isBefore(today) && unpaidAmount > 0) {
                    double overdueRate = loan.getOverdueInterest();
                    double dailyOverdue = (unpaidAmount * overdueRate) / (100 * DAYS_IN_YEAR);
                    dailyOverdue = round(dailyOverdue);

                    s.setStatus("OVERDUE");
                    loan.setStatus("OVERDUE");

                    accuralsService.saveOverDue(
                            loan.getLoanNumber(), s.getId(), today, dailyOverdue, s.getEmiMonth()
                    );

                    int graceDays = 1;
                    LocalDate penaltyGraceDate = s.getEmiDate().plusDays(graceDays);

                    if (today.isAfter(penaltyGraceDate)) {
                        double lateFee = loan.getLateFeeCharge();
                        accuralsService.saveLateFee(loan.getLoanNumber(), s.getId(), today, lateFee, s.getEmiMonth());
                    }

                    if (today.isAfter(penaltyGraceDate)) {
                        double penaltyRate = loan.getPenaltyInterest();
                        double dailyPenalty = (unpaidAmount * penaltyRate) / (100 * DAYS_IN_YEAR);
                        dailyPenalty = round(dailyPenalty);
                        accuralsService.savePenalty(
                                loan.getLoanNumber(), s.getId(), today, dailyPenalty, s.getEmiMonth()
                        );
                    }

                    int defaultingPeriod = loan.getDefaultingPeriod();
                    if (today.isAfter(s.getEmiDate().plusDays(defaultingPeriod))) {
                        s.setStatus("DEFAULT");
                        loan.setStatus("DEFAULT");
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
