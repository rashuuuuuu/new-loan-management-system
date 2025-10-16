package com.rashmita.accuralsservice.service.AccuralsServiceImpl;

import com.rashmita.accuralsservice.service.TotalPayable;
import com.rashmita.commoncommon.entity.*;
import com.rashmita.commoncommon.model.CreateTotalAccrual;
import com.rashmita.commoncommon.model.LoanNumberModel;
import com.rashmita.commoncommon.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class TotalPayableImpl implements TotalPayable {
    private static final Logger log = LoggerFactory.getLogger(TotalPayableImpl.class);
    private final EmiOverdueRepository emiOverdueRepository;
    private final EmiPenaltyRepository emiPenaltyRepository;
    private final EmiLateFeeRepository emiLateFeeRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final EmiInterestRepository emiInterestRepository;
    public TotalPayableImpl(EmiOverdueRepository emiOverdueRepository,
                            EmiPenaltyRepository emiPenaltyRepository,
                            EmiLateFeeRepository emiLateFeeRepository,
                            EmiScheduleRepository emiScheduleRepository, EmiInterestRepository emiInterestRepository) {
        this.emiOverdueRepository = emiOverdueRepository;
        this.emiPenaltyRepository = emiPenaltyRepository;
        this.emiLateFeeRepository = emiLateFeeRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.emiInterestRepository = emiInterestRepository;
    }

    @Override
    public double calculateTotalOverdue(String loanNumber) {
        List<EmiOverdue> emiOverdues = emiOverdueRepository.getAllByLoanNumber(loanNumber);
        return emiOverdues.stream()
                .mapToDouble(EmiOverdue::getOverdueAmount)
                .sum();
    }

    @Override
    public double calculateTotalPenalty(String loanNumber) {
        List<EmiPenalty> emiPenalties = emiPenaltyRepository.findByLoanNumber(loanNumber);
        return emiPenalties.stream()
                .mapToDouble(EmiPenalty::getPenaltyAmount)
                .sum();
    }

    @Override
    public double calculateTotalLateFee(String loanNumber) {
        List<EmiLateFee> lateFees = emiLateFeeRepository.findByLoanNumber(loanNumber);
        if (lateFees == null || lateFees.isEmpty()) {
            return 0.0;
        }
        return lateFees.get(0).getLateFee();
    }

    @Override
    public double calculateTotalInterest(String loanNumber) {
        List<EmiInterest> emiInterests=emiInterestRepository.findByLoanNumber(loanNumber);
        return emiInterests.stream()
                .mapToDouble(EmiInterest::getInterestAmount)
                .sum();
    }

    public double calculateUnpaidEmiAmount(String loanNumber) {
        return emiScheduleRepository.findByLoanNumber(loanNumber)
                .stream()
                .filter(emi -> !"PAID".equalsIgnoreCase(emi.getStatus()))
                .mapToDouble(EmiSchedule::getEmiAmount)
                .sum();
    }
    @Override
    public double calculateTotalPayable(String loanNumber) {
        double overdue = calculateTotalOverdue(loanNumber);
        double penalty = calculateTotalPenalty(loanNumber);
        double lateFee = calculateTotalLateFee(loanNumber);
        double interest = calculateTotalInterest(loanNumber);
        double unpaid = calculateUnpaidEmiAmount(loanNumber);
        double total = overdue + penalty + lateFee + unpaid+ interest;
        log.info("Total payable for loan {} = {}", loanNumber, total);
        return total;
    }
    @Override
    public CreateTotalAccrual calculateAccrualsByLoanNumber(LoanNumberModel loanNumberModel) {
        double totalAccrualsPerLoanNumber= Stream.of(
                        calculateTotalOverdue(loanNumberModel.getLoanNumber()),
                        calculateTotalPenalty(loanNumberModel.getLoanNumber()),
                        calculateTotalLateFee(loanNumberModel.getLoanNumber()),
                        calculateTotalInterest(loanNumberModel.getLoanNumber())
                )
                .filter(Double::isFinite)
                .mapToDouble(Double::doubleValue)
                .sum();
        CreateTotalAccrual createTotalAccrual=new CreateTotalAccrual();
        createTotalAccrual.setTotalAccruals(totalAccrualsPerLoanNumber);
        return createTotalAccrual;
    }
}
