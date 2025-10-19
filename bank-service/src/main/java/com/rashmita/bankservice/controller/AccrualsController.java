package com.rashmita.bankservice.controller;
import com.rashmita.bankservice.client.AccrualsClient;
import com.rashmita.bankservice.mapper.LoanReportDto;
import com.rashmita.bankservice.model.LoanNumberModel;
import com.rashmita.common.constants.ApiConstants;
import com.rashmita.common.model.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.ACCRUALS)
@RequiredArgsConstructor
public class AccrualsController {
    private final AccrualsClient accrualsClient;
    @PostMapping("/by/loannumber")
    @PreAuthorize("hasAuthority('REPORT_VIEWER')")
    public ServerResponse<?> getAccrual(@RequestBody LoanNumberModel loanNumber) {
        return accrualsClient.createTotalAccrualsByLoanNumber(loanNumber);
    }

  @GetMapping("/report")
    public List<LoanReportDto> getTotalAccrualsByLoanNumber() {
        return accrualsClient.getTotalAccrualsByLoanNumber();
  }
}
