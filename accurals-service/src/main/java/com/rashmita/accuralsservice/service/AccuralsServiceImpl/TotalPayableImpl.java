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
    private final TotalAccruedRepository totalAccruedRepository;
    private final TotalPayableRepository totalPayableRepository;

    public TotalPayableImpl(EmiOverdueRepository emiOverdueRepository,
                            EmiPenaltyRepository emiPenaltyRepository,
                            EmiLateFeeRepository emiLateFeeRepository,
                            EmiScheduleRepository emiScheduleRepository, EmiInterestRepository emiInterestRepository, LoanDetailsRepository loanDetailsRepository, TotalAccruedRepository totalAccruedRepository, TotalPayableRepository totalPayableRepository) {
        this.emiOverdueRepository = emiOverdueRepository;
        this.emiPenaltyRepository = emiPenaltyRepository;
        this.emiLateFeeRepository = emiLateFeeRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.emiInterestRepository = emiInterestRepository;
        this.loanDetailsRepository = loanDetailsRepository;
        this.totalAccruedRepository = totalAccruedRepository;
        this.totalPayableRepository = totalPayableRepository;
    }

    @Override
    public double calculateTotalOverdue(String loanNumber) {
        List<EmiOverdue> emiOverdues = emiOverdueRepository.getAllByLoanNumber(loanNumber);
        return emiOverdues.stream()
                .mapToDouble(EmiOverdue::getOverdueAmount)
                .sum();
    }

    private double calculateTotalOverduePerMonth(String loanNumber, int emiMonth) {
        List<EmiOverdue> emiOverdues = emiOverdueRepository.getAllByLoanNumber(loanNumber);

        return emiOverdues.stream()
                .filter(emi -> emi.getEmiMonth() == emiMonth)
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

    private double calculateTotalPenaltyPerMonth(String loanNumber, int emiMonth) {
        List<EmiPenalty> emiPenalties = emiPenaltyRepository.findByLoanNumber(loanNumber);
        return emiPenalties.stream()
                .filter(emi -> emi.getEmiMonth() == emiMonth)
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

    private double calculateTotalLateFeePerMonth(String loanNumber, int emiMonth) {
        List<EmiLateFee> lateFees = emiLateFeeRepository.findByLoanNumber(loanNumber);
        return lateFees.stream()
                .filter(emi -> emi.getEmiMonth() == emiMonth)
                .map(EmiLateFee::getLateFee)
                .findFirst()
                .orElse(0.0); // returns 0 if no late fee exists for that month
    }

    @Override
    public double calculateTotalInterest(String loanNumber) {
        List<EmiInterest> emiInterests = emiInterestRepository.findByLoanNumber(loanNumber);
        return emiInterests.stream()
                .mapToDouble(EmiInterest::getInterestAmount)
                .sum();
    }

    private double calculateTotalInterestPerMonth(String loanNumber, int emiMonth) {
        List<EmiInterest> emiInterests = emiInterestRepository.findByLoanNumber(loanNumber);
        return emiInterests.stream()
                .filter(emi -> emi.getEmiMonth() == emiMonth)
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

//    @Override
//    public double calculateTotalPayable(String loanNumber) {
//        double overdue = calculateTotalOverdue(loanNumber);
//        double penalty = calculateTotalPenalty(loanNumber);
//        double lateFee = calculateTotalLateFee(loanNumber);
//        double interest = calculateTotalInterest(loanNumber);
//        double unpaid = calculateUnpaidEmiAmount(loanNumber);
//        double total = overdue + penalty + lateFee + unpaid + interest;
//        log.info("Total payable for loan {} = {}", loanNumber, total);
//        return total;
//    }


    //    public String createTotalAccruedEntity(String loanNumber) {
//        List<TotalAccruedEntity> emiEntities = new ArrayList<>();
//        List<EmiSchedule> emiSchedule = emiScheduleRepository.findByLoanNumber(loanNumber);
//        int tenureMonth = emiSchedule.getLast().getEmiMonth();
//        for (int i = 1; i < tenureMonth; i++) {
//            TotalAccruedEntity totalAccruedEntity = new TotalAccruedEntity();
//            totalAccruedEntity.setLoanNumber(loanNumber);
//            totalAccruedEntity.setTenure(tenureMonth);
//            totalAccruedEntity.setEmiAmount(emiSchedule.get(i).getEmiAmount());
//            totalAccruedEntity.setPayableInterest(calculateTotalInterestPerMonth(loanNumber, i));
//            totalAccruedEntity.setPayablePenalty(calculateTotalPenaltyPerMonth(loanNumber, i));
//            totalAccruedEntity.setPayableLateFee(calculateTotalLateFeePerMonth(loanNumber, i));
//            totalAccruedEntity.setPayableOverdue(calculateTotalOverduePerMonth(loanNumber, i));
//            emiEntities.add(totalAccruedEntity);
//        }
//        totalAccruedRepository.saveAll(emiEntities);
//        return "total accrued calculated per month";
//    }
    public String createTotalAccruedEntity(LoanNumberModel loanNumber) {
        List<TotalAccruedEntity> emiEntities = new ArrayList<>();
        List<EmiSchedule> emiSchedule = emiScheduleRepository.findByLoanNumber(loanNumber.getLoanNumber());
        int tenureMonth = emiSchedule.getLast().getEmiMonth();
        for (int i = 1; i < tenureMonth; i++) {
            TotalAccruedEntity totalAccruedEntity = new TotalAccruedEntity();
            totalAccruedEntity.setLoanNumber(loanNumber.getLoanNumber());
            totalAccruedEntity.setTenure(tenureMonth);
            totalAccruedEntity.setEmiAmount(emiSchedule.get(i).getEmiAmount());
            totalAccruedEntity.setPayableInterest(calculateTotalInterestPerMonth(loanNumber.getLoanNumber(), i));
            totalAccruedEntity.setPayablePenalty(calculateTotalPenaltyPerMonth(loanNumber.getLoanNumber(), i));
            totalAccruedEntity.setPayableLateFee(calculateTotalLateFeePerMonth(loanNumber.getLoanNumber(), i));
            totalAccruedEntity.setPayableOverdue(calculateTotalOverduePerMonth(loanNumber.getLoanNumber(), i));
            emiEntities.add(totalAccruedEntity);
        }
        totalAccruedRepository.saveAll(emiEntities);
        return "total accrued calculated per month";
    }

    public String updateTotalAccruedEntity(LoanNumberModel loanNumber) {
        if (totalAccruedRepository.existsByLoanNumber(loanNumber.getLoanNumber())) {
            List<TotalAccruedEntity> emiEntities = new ArrayList<>();
            List<EmiSchedule> emiSchedule = emiScheduleRepository.findByLoanNumber(loanNumber.getLoanNumber());
            int tenureMonth = emiSchedule.getLast().getEmiMonth();
            for (int i = 1; i < tenureMonth; i++) {
                TotalAccruedEntity totalAccruedEntity = (TotalAccruedEntity) totalAccruedRepository.findByLoanNumber(loanNumber.getLoanNumber());
                totalAccruedEntity.setEmiAmount(emiSchedule.get(i).getEmiAmount());
                totalAccruedEntity.setPayableInterest(calculateTotalInterestPerMonth(loanNumber.getLoanNumber(), i));
                totalAccruedEntity.setPayablePenalty(calculateTotalPenaltyPerMonth(loanNumber.getLoanNumber(), i));
                totalAccruedEntity.setPayableLateFee(calculateTotalLateFeePerMonth(loanNumber.getLoanNumber(), i));
                totalAccruedEntity.setPayableOverdue(calculateTotalOverduePerMonth(loanNumber.getLoanNumber(), i));
                emiEntities.add(totalAccruedEntity);
            }
            totalAccruedRepository.saveAll(emiEntities);
        }
        return "total accrued updated per month";
    }

    //
//    public void totalPayablePerMonth(String loanNumber) {
//        List<EmiSchedule> emiScheduleList = emiScheduleRepository.findByLoanNumber(loanNumber);
//        if (emiScheduleList == null || emiScheduleList.isEmpty()) {
//            log.warn("No EMI schedule found for loan number {}", loanNumber);
//            return;
//        }
//        int tenureMonth = emiScheduleList.get(emiScheduleList.size() - 1).getEmiMonth();
//        List<TotalPayableEntity> totalPayables = new ArrayList<>();
//        for (int i = 1; i <= tenureMonth; i++) {
//            EmiSchedule schedule = emiScheduleList.get(i - 1);
//            TotalPayableEntity totalPayableEntity = totalPayableRepository
//                    .findByLoanNumberAndTenure(loanNumber, i)
//                    .orElse(new TotalPayableEntity());
//            totalPayableEntity.setLoanNumber(loanNumber);
//            totalPayableEntity.setTenure(i);
//            totalPayableEntity.setEmiAmount(schedule.getEmiAmount());
//            totalPayableEntity.setEmiDate(schedule.getEmiDate());
//            totalPayableEntity.setPayableInterest(calculateTotalInterestPerMonth(loanNumber, i));
//            totalPayableEntity.setPayableLateFee(calculateTotalLateFeePerMonth(loanNumber, i));
//            totalPayableEntity.setPayableOverdue(calculateTotalOverduePerMonth(loanNumber, i));
//            totalPayableEntity.setPayablePenalty(calculateTotalPenaltyPerMonth(loanNumber, i));
//            if (tenureMonth == 1) {
//                totalPayableEntity.setPayablePenalty(0.0);
//                totalPayableEntity.setPayableLateFee(0.0);
//                totalPayableEntity.setPayableOverdue(0.0);
//                totalPayableEntity.setPayableInterest(0.0);
//            }
//
//            if (Boolean.TRUE.equals(schedule.getLastInstallment())) {
//                Double payablePenalty = calculateTotalPenaltyPerMonth(loanNumber, i) + calculateTotalPenaltyPerMonth(loanNumber, tenureMonth);
//                Double payableInterest = calculateTotalInterestPerMonth(loanNumber, i) + calculateTotalInterestPerMonth(loanNumber, tenureMonth);
//                Double payableLateFee = calculateTotalLateFeePerMonth(loanNumber, i) + calculateTotalLateFeePerMonth(loanNumber, tenureMonth);
//                Double payableOverdue = calculateTotalOverduePerMonth(loanNumber, i) + calculateTotalOverduePerMonth(loanNumber, tenureMonth);
//
//                totalPayableEntity.setPayablePenalty(payablePenalty);
//                totalPayableEntity.setPayableInterest(payableInterest);
//                totalPayableEntity.setPayableLateFee(payableLateFee);
//                totalPayableEntity.setPayableOverdue(payableOverdue);
//            }
//            double totalPayable = totalPayableEntity.getEmiAmount()
//                    + totalPayableEntity.getPayableInterest()
//                    + totalPayableEntity.getPayableLateFee()
//                    + totalPayableEntity.getPayableOverdue()
//                    + (totalPayableEntity.getPayablePenalty() != null
//                    ? totalPayableEntity.getPayablePenalty() : 0.0);
//
//            totalPayableEntity.setTotalPayable(totalPayable);
//            if (totalPayableEntity.getPaidInterest() == null) totalPayableEntity.setPaidInterest(0.0);
//            if (totalPayableEntity.getPaidOverdue() == null) totalPayableEntity.setPaidOverdue(0.0);
//            if (totalPayableEntity.getPaidLateFee() == null) totalPayableEntity.setPaidLateFee(0.0);
//            if (totalPayableEntity.getPaidPenalty() == null) totalPayableEntity.setPaidPenalty(0.0);
//            if (totalPayableEntity.getPaidPrincipal() == null) totalPayableEntity.setPaidPrincipal(0.0);
//            if (totalPayableEntity.getTotalPayable() == 0.0) {
//                totalPayableEntity.setStatus("PAID");
//            } else {
//                totalPayableEntity.setStatus("UNPAID");
//            }
//
//            totalPayables.add(totalPayableEntity);
//        }
//        totalPayableRepository.saveAll(totalPayables);
//        log.info("Total payable updated for loan {}", loanNumber);
//    }
//
    public void totalPayablePerMonth(String loanNumber) {
        List<EmiSchedule> emiScheduleList = emiScheduleRepository.findByLoanNumber(loanNumber);
        Optional<LoanDetails> loanDetails = loanDetailsRepository.findByLoanNumber(loanNumber);

        if (loanDetails.isEmpty() || emiScheduleList.isEmpty()) {
            return;
        }
        int tenureMonth = loanDetails.get().getTenure();
        List<TotalPayableEntity> totalPayables = new ArrayList<>();
        Double calculatedEmiOverdue = 0.0;
        Double calculatedEmiPenalty = 0.0;
        Double calculatedEmiLateFee = 0.0;
        for (int i = 0; i < tenureMonth; i++) {
            EmiSchedule schedule = emiScheduleList.get(i);
            TotalPayableEntity totalPayableEntity = totalPayableRepository
                    .findByLoanNumberAndTenure(loanNumber, i + 1) // tenure is 1-indexed
                    .orElse(new TotalPayableEntity());
            totalPayableEntity.setLoanNumber(loanNumber);
            totalPayableEntity.setTenure(i + 1);
            totalPayableEntity.setEmiDate(schedule.getEmiDate());
            totalPayableEntity.setStatus(schedule.getStatus());
            totalPayableEntity.setEmiAmount(schedule.getEmiAmount());
            totalPayableEntity.setPayablePrincipal(schedule.getPrincipalComponent());
            totalPayableEntity.setPayableInterest(calculateTotalInterestPerMonth(loanNumber, i + 1));
            totalPayableEntity.setPayableLateFee(calculatedEmiLateFee);
            totalPayableEntity.setPayableOverdue(calculatedEmiOverdue);
            totalPayableEntity.setPayablePenalty(calculatedEmiPenalty);
            calculatedEmiLateFee = calculateTotalLateFeePerMonth(loanNumber, i + 1);
            calculatedEmiOverdue = calculateTotalOverduePerMonth(loanNumber, i + 1);
            calculatedEmiPenalty = calculateTotalPenaltyPerMonth(loanNumber, i + 1);
            if (Boolean.TRUE.equals(schedule.getLastInstallment())) {
                Double payablePenalty = calculateTotalPenaltyPerMonth(loanNumber, tenureMonth - 1)
                        + calculateTotalPenaltyPerMonth(loanNumber, tenureMonth);
                Double payableInterest = calculateTotalInterestPerMonth(loanNumber, tenureMonth);
                Double payableLateFee = calculateTotalLateFeePerMonth(loanNumber, tenureMonth - 1)
                        + calculateTotalLateFeePerMonth(loanNumber, tenureMonth);
                Double payableOverdue = calculateTotalOverduePerMonth(loanNumber, tenureMonth - 1)
                        + calculateTotalOverduePerMonth(loanNumber, tenureMonth);
                totalPayableEntity.setPayablePenalty(payablePenalty);
                totalPayableEntity.setPayableInterest(payableInterest);
                totalPayableEntity.setPayableLateFee(payableLateFee);
                totalPayableEntity.setPayableOverdue(payableOverdue);
            }
            double totalPayable = totalPayableEntity.getPayablePrincipal()
                    + totalPayableEntity.getPayableInterest()
                    + totalPayableEntity.getPayableLateFee()
                    + totalPayableEntity.getPayableOverdue()
                    + (totalPayableEntity.getPayablePenalty() != null
                    ? totalPayableEntity.getPayablePenalty() : 0.0);
            totalPayableEntity.setTotalPayable(totalPayable);
            if (totalPayableEntity.getPaidInterest() == null) totalPayableEntity.setPaidInterest(0.0);
            if (totalPayableEntity.getPaidOverdue() == null) totalPayableEntity.setPaidOverdue(0.0);
            if (totalPayableEntity.getPaidLateFee() == null) totalPayableEntity.setPaidLateFee(0.0);
            if (totalPayableEntity.getPaidPenalty() == null) totalPayableEntity.setPaidPenalty(0.0);
            if (totalPayableEntity.getPaidPrincipal() == null) totalPayableEntity.setPaidPrincipal(0.0);
            if (totalPayableEntity.getTotalPayable() == 0.0) {
                totalPayableEntity.setStatus("PAID");
            } else {
                totalPayableEntity.setStatus("UNPAID");
            }
            totalPayables.add(totalPayableEntity);
        }
        log.info("total payable updated successfully");
        totalPayableRepository.saveAll(totalPayables);
    }


    public String totalPayablePerMonth(LoanNumberModel loanNumber) {
        List<EmiSchedule> emiScheduleList = emiScheduleRepository.findByLoanNumber(loanNumber.getLoanNumber());
        Optional<LoanDetails> loanDetails = loanDetailsRepository.findByLoanNumber(loanNumber.getLoanNumber());
        int tenureMonth = loanDetails.get().getTenure();
        List<TotalPayableEntity> totalPayables = new ArrayList<>();
        for (int i = 0; i < tenureMonth; i++) {
            EmiSchedule schedule = emiScheduleList.get(i);
            TotalPayableEntity totalPayableEntity = new TotalPayableEntity();
            totalPayableEntity.setLoanNumber(loanNumber.getLoanNumber());
            totalPayableEntity.setTenure(i + 1);
            totalPayableEntity.setEmiDate(schedule.getEmiDate());
            totalPayableEntity.setStatus(schedule.getStatus());
            totalPayableEntity.setEmiAmount(schedule.getEmiAmount());
            totalPayableEntity.setPayableInterest(calculateTotalInterestPerMonth(loanNumber.getLoanNumber(), i));
            totalPayableEntity.setPayableLateFee(calculateTotalLateFeePerMonth(loanNumber.getLoanNumber(), i));
            totalPayableEntity.setPayableOverdue(calculateTotalOverduePerMonth(loanNumber.getLoanNumber(), i));
            totalPayableEntity.setPayablePenalty(calculateTotalPenaltyPerMonth(loanNumber.getLoanNumber(), i));
            if (tenureMonth == 1) {
                totalPayableEntity.setPayablePenalty(0.0);
                totalPayableEntity.setPayableLateFee(0.0);
                totalPayableEntity.setPayableOverdue(0.0);
                totalPayableEntity.setPayableInterest(0.0);
            }
            if (Boolean.TRUE.equals(schedule.getLastInstallment())) {
                Double payablePenalty = calculateTotalPenaltyPerMonth(loanNumber.getLoanNumber(), tenureMonth - 1) + calculateTotalPenaltyPerMonth(loanNumber.getLoanNumber(), tenureMonth);
                Double payableInterest = calculateTotalInterestPerMonth(loanNumber.getLoanNumber(), tenureMonth - 1) + calculateTotalInterestPerMonth(loanNumber.getLoanNumber(), tenureMonth);
                Double payableLateFee = calculateTotalLateFeePerMonth(loanNumber.getLoanNumber(), tenureMonth - 1) + calculateTotalLateFeePerMonth(loanNumber.getLoanNumber(), tenureMonth);
                Double payableOverdue = calculateTotalOverduePerMonth(loanNumber.getLoanNumber(), tenureMonth - 1) + calculateTotalOverduePerMonth(loanNumber.getLoanNumber(), tenureMonth);
                totalPayableEntity.setPayablePenalty(payablePenalty);
                totalPayableEntity.setPayableInterest(payableInterest);
                totalPayableEntity.setPayableLateFee(payableLateFee);
                totalPayableEntity.setPayableOverdue(payableOverdue);
            }
            double totalPayable = totalPayableEntity.getEmiAmount()
                    + totalPayableEntity.getPayableInterest()
                    + totalPayableEntity.getPayableLateFee()
                    + totalPayableEntity.getPayableOverdue()
                    + (totalPayableEntity.getPayablePenalty() != null
                    ? totalPayableEntity.getPayablePenalty() : 0.0);
            totalPayableEntity.setTotalPayable(totalPayable);
            totalPayables.add(totalPayableEntity);
        }
        totalPayableRepository.saveAll(totalPayables);
        return "totalpayable created successfully";
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
//                .toList();
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

                    Map<Integer, Map<String, Object>> emiBreakdown = new LinkedHashMap<>();

                    for (Integer month : allMonths) {
                        Map<String, Object> breakdown = new HashMap<>();

                        double overdue = overdueList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .mapToDouble(EmiOverdue::getOverdueAmount)
                                .sum();

                        double penalty = penaltyList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .mapToDouble(EmiPenalty::getPenaltyAmount)
                                .sum();

                        // late fee is charge only once per loan
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

                        EmiSchedule schedule = scheduleList.stream()
                                .filter(e -> Objects.equals(e.getEmiMonth(), month))
                                .findFirst()
                                .orElse(null);

                        breakdown.put("overdue", overdue);
                        breakdown.put("penalty", penalty);
                        breakdown.put("lateFee", lateFee);
                        breakdown.put("interest", interest);
                        breakdown.put("unpaidEmi", unpaid);
                        breakdown.put("total", overdue + penalty + lateFee + interest + unpaid);
                        if (schedule != null) {
                            breakdown.put("emiStartDate", schedule.getEmiStartDate());
                            breakdown.put("emiDate", schedule.getEmiDate());
                        }
                        emiBreakdown.put(month, breakdown);
                    }
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
                    dto.setTenure(loan.getTenure());
                    dto.setEmiSummary(emiBreakdown);

                    return dto;
                })
                .toList();
    }

}


