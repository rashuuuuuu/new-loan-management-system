package com.rashmita.accuralsservice.service.AccuralsServiceImpl;

import com.rashmita.accuralsservice.service.TotalPayable;
import com.rashmita.commoncommon.entity.*;
import com.rashmita.commoncommon.model.CreateTotalAccrual;
import com.rashmita.commoncommon.model.LoanNumberModel;
import com.rashmita.commoncommon.model.LoanReportDto;
import com.rashmita.commoncommon.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TotalPayableImpl implements TotalPayable {
    private static final Logger log = LoggerFactory.getLogger(TotalPayableImpl.class);
    private final EmiOverdueRepository emiOverdueRepository;
    private final EmiPenaltyRepository emiPenaltyRepository;
    private final EmiLateFeeRepository emiLateFeeRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final EmiInterestRepository emiInterestRepository;
    private final LoanDetailsRepository loanDetailsRepository;

    public TotalPayableImpl(EmiOverdueRepository emiOverdueRepository,
                            EmiPenaltyRepository emiPenaltyRepository,
                            EmiLateFeeRepository emiLateFeeRepository,
                            EmiScheduleRepository emiScheduleRepository, EmiInterestRepository emiInterestRepository, LoanDetailsRepository loanDetailsRepository) {
        this.emiOverdueRepository = emiOverdueRepository;
        this.emiPenaltyRepository = emiPenaltyRepository;
        this.emiLateFeeRepository = emiLateFeeRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.emiInterestRepository = emiInterestRepository;
        this.loanDetailsRepository = loanDetailsRepository;
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
        List<EmiInterest> emiInterests = emiInterestRepository.findByLoanNumber(loanNumber);
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
        double total = overdue + penalty + lateFee + unpaid + interest;
        log.info("Total payable for loan {} = {}", loanNumber, total);
        return total;
    }

//    @Override
//    public List<LoanReportDto> getAllReport() {
//        List<LoanDetails> allLoanDetails = loanDetailsRepository.findAll();
//        return allLoanDetails.stream()
//                .map(loandetails -> {
//                    String loanNumber = loandetails.getLoanNumber();
//                    String customerNumber = loandetails.getCustomerNumber();
//                    String bankCode = loandetails.getBankCode();
//                    double overdue = calculateTotalOverdue(loanNumber);
//                    double penalty = calculateTotalPenalty(loanNumber);
//                    double lateFee = calculateTotalLateFee(loanNumber); // use stream sum
//                    double interest = calculateTotalInterest(loanNumber);
//                    double totalPayable = Stream.of(
//                                    calculateTotalOverdue(loanNumber),
//                                    calculateTotalPenalty(loanNumber),
//                                    calculateTotalLateFee(loanNumber),
//                                    calculateTotalInterest(loanNumber)
//                            )
//                            .filter(Double::isFinite)
//                            .mapToDouble(Double::doubleValue)
//                            .sum();
//
//                    LoanReportDto dto = new LoanReportDto();
//                    dto.setLoanNumber(loanNumber);
//                    dto.setCustomerNumber(customerNumber);
//                    dto.setBankCode(bankCode);
//                    dto.setTotalOverdue(overdue);
//                    dto.setTotalPenalty(penalty);
//                    dto.setTotalLateFee(lateFee);
//                    dto.setTotalInterest(interest);
//                    dto.setTotalAccruals(totalPayable);
//                    return dto;
//                })
//                .toList(); // no need for distinct if you are mapping per loan
//    }


    @Override
    public CreateTotalAccrual calculateAccrualsByLoanNumber(LoanNumberModel loanNumberModel) {
        double totalAccrualsPerLoanNumber = Stream.of(
                        calculateTotalOverdue(loanNumberModel.getLoanNumber()),
                        calculateTotalPenalty(loanNumberModel.getLoanNumber()),
                        calculateTotalLateFee(loanNumberModel.getLoanNumber()),
                        calculateTotalInterest(loanNumberModel.getLoanNumber())
                )
                .filter(Double::isFinite)
                .mapToDouble(Double::doubleValue)
                .sum();
        CreateTotalAccrual createTotalAccrual = new CreateTotalAccrual();
        createTotalAccrual.setTotalAccruals(totalAccrualsPerLoanNumber);
        return createTotalAccrual;
    }

    @Override
    public List<LoanReportDto> getAllReport() {
        List<LoanDetails> allLoans = loanDetailsRepository.findAll();

        Map<String, LoanDetails> uniqueLoans = allLoans.stream()
                .collect(Collectors.toMap(LoanDetails::getLoanNumber, l -> l, (l1, l2) -> l1));

        return uniqueLoans.values().stream()
                .map(loan -> {
                    String loanNumber = loan.getLoanNumber();

                    List<EmiOverdue> overdueList = emiOverdueRepository.getAllByLoanNumber(loanNumber);
                    List<EmiPenalty> penaltyList = emiPenaltyRepository.findByLoanNumber(loanNumber);
                    List<EmiLateFee> lateFeeList = emiLateFeeRepository.findByLoanNumber(loanNumber);
                    List<EmiInterest> interestList = emiInterestRepository.findByLoanNumber(loanNumber);
                    List<EmiSchedule> scheduleList = emiScheduleRepository.findByLoanNumber(loanNumber);

                    Set<Integer> allMonths = Stream.of(
                                    overdueList.stream().map(EmiOverdue::getEmiMonth),
                                    penaltyList.stream().map(EmiPenalty::getEmiMonth),
                                    lateFeeList.stream().map(EmiLateFee::getEmiMonth),
                                    interestList.stream().map(EmiInterest::getEmiMonth),
                                    scheduleList.stream().map(EmiSchedule::getEmiMonth)
                            ).flatMap(s -> s)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toCollection(TreeSet::new));

                    Map<Integer, Map<String, Double>> emiBreakdown = new LinkedHashMap<>();

                    for (Integer month : allMonths) {
                        Map<String, Double> breakdown = new HashMap<>();

                        double overdue = overdueList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .mapToDouble(EmiOverdue::getOverdueAmount)
                                .sum();

                        double penalty = penaltyList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .mapToDouble(EmiPenalty::getPenaltyAmount)
                                .sum();

                        // Take only one late fee per EMI month
                        double lateFee = lateFeeList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .findFirst()
                                .map(EmiLateFee::getLateFee)
                                .orElse(0.0);

                        double interest = interestList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .mapToDouble(EmiInterest::getInterestAmount)
                                .sum();

                        double unpaid = scheduleList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .filter(e -> !"PAID".equalsIgnoreCase(e.getStatus()))
                                .mapToDouble(EmiSchedule::getEmiAmount)
                                .sum();

                        breakdown.put("overdue", overdue);
                        breakdown.put("penalty", penalty);
                        breakdown.put("lateFee", lateFee);
                        breakdown.put("interest", interest);
                        breakdown.put("unpaidEmi", unpaid);
                        breakdown.put("total", overdue + penalty + lateFee + interest + unpaid);
                        emiBreakdown.put(month, breakdown);
                    }

                    // Grand totals
                    double totalOverdue = overdueList.stream().mapToDouble(EmiOverdue::getOverdueAmount).sum();
                    double totalPenalty = penaltyList.stream().mapToDouble(EmiPenalty::getPenaltyAmount).sum();
                    double totalLateFee = lateFeeList.stream().map(EmiLateFee::getLateFee).findFirst().orElse(0.0);
                    double totalInterest = interestList.stream().mapToDouble(EmiInterest::getInterestAmount).sum();
                    double totalUnpaid = scheduleList.stream()
                            .filter(e -> !"PAID".equalsIgnoreCase(e.getStatus()))
                            .mapToDouble(EmiSchedule::getEmiAmount)
                            .sum();
                    double totalAccruals = totalOverdue + totalPenalty + totalLateFee + totalInterest + totalUnpaid;

                    LoanReportDto dto = new LoanReportDto();
                    dto.setStatus(loan.getStatus());
                    dto.setLoanNumber(loanNumber);
                    dto.setCustomerNumber(loan.getCustomerNumber());
                    dto.setBankCode(loan.getBankCode());
                    dto.setTotalOverdue(totalOverdue);
                    dto.setTotalPenalty(totalPenalty);
                    dto.setTotalLateFee(totalLateFee);
                    dto.setTotalInterest(totalInterest);
                    dto.setTotalAccruals(totalAccruals);
                    dto.setEmiSummary(emiBreakdown);

                    return dto;
                })
                .toList();
    }

}


