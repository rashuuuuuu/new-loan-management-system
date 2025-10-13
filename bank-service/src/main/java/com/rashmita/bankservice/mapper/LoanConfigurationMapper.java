package com.rashmita.bankservice.mapper;

import com.rashmita.common.entity.Bank;
import com.rashmita.common.entity.LoanConfiguration;
import com.rashmita.bankservice.model.LoanConfigBankCodeRequest;
import com.rashmita.bankservice.model.LoanConfigurationRequest;
import com.rashmita.bankservice.model.LoanConfigurationResponse;
import com.rashmita.bankservice.model.LoanUpdateRequest;
import com.rashmita.bankservice.repository.LoanConfigurationRepository;
import com.rashmita.common.constants.StatusConstants;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanConfigurationMapper {
    /**
     * rashmita subedi
     */
    @Autowired
    private final ModelMapper modelMapper;
    private final LoanConfigurationRepository loanConfigurationRepository;
    private final BankRepository bankRepository;
    public LoanConfiguration saveLoanConfiguration(LoanConfigurationRequest request) {
        Bank bank = bankRepository.findByBankCode(request.getBankIdRequest().getBankCode())
                .orElseThrow(() -> new RuntimeException(
                        "Bank not found with code: " + request.getBankIdRequest().getBankCode()));

        LoanConfiguration lc = new LoanConfiguration();
        lc.setMinimumAmount(request.getMinimumAmount());
        lc.setMaximumAmount(request.getMaximumAmount());
        lc.setInterestRate(request.getInterestRate());
        lc.setLateFeeCharge(request.getLateFeeCharge());
        lc.setLoanAdministrationFeeRate(request.getLoanAdministrationFeeRate());
        lc.setLoanAdministrationFeeAmount(request.getLoanAdministrationFeeAmount());
        lc.setDefaultingPeriod(request.getDefaultingPeriod());
        lc.setMaximumLoanPeriod(request.getMaximumLoanPeriod());
        lc.setModifiedDate(new Date());
        lc.setStatus(StatusConstants.CREATED);

        // Set the bank
        lc.setBank(bank);

        return loanConfigurationRepository.save(lc);
    }

    public LoanConfiguration updateLoanConfiguration(LoanUpdateRequest loanUpdateRequest) {
        Long id = loanUpdateRequest.getId();
        LoanConfiguration loanConfiguration = loanConfigurationRepository.getById(id);
        if (loanUpdateRequest.getId() != null) {
            loanConfiguration.setMaximumLoanPeriod(loanUpdateRequest.getMaximumLoanPeriod());
            loanConfiguration.setLoanAdministrationFeeAmount(loanUpdateRequest.getLoanAdministrationFeeAmount());
            loanConfiguration.setDefaultingPeriod(loanUpdateRequest.getDefaultingPeriod());
            loanConfiguration.setModifiedDate(loanUpdateRequest.getModifiedDate());
            loanConfiguration.setStatus(StatusConstants.UPDATED);
            loanConfiguration.setLoanAdministrationFeeRate(loanUpdateRequest.getLoanAdministrationFeeRate());
            loanConfiguration.setMaximumAmount(loanUpdateRequest.getMaximumAmount());
            loanConfiguration.setMinimumAmount(loanUpdateRequest.getMinimumAmount());
            loanConfiguration.setInterestRate(loanUpdateRequest.getInterestRate());
            loanConfiguration.setLateFeeCharge(loanUpdateRequest.getLateFeeCharge());
        }
        return loanConfigurationRepository.save(loanConfiguration);
    }
//    public LoanConfigurationResponse getDetailsByBankCode(LoanConfigBankCodeRequest request) throws NotFoundException {
//        LoanConfiguration loanConfig = loanConfigurationRepository.findByBankCode(request.getBankCode())
//                .orElseThrow(() -> new NotFoundException("Loan configuration not found"));
//
//        if (loanConfig.getStatus() == StatusConstants.DELETED) {
//            throw new NotFoundException("Loan configuration is unavailable");
//        }
//
//        return modelMapper.map(loanConfig, LoanConfigurationResponse.class);
//    }
    public void deleteLoanConfiguration(LoanConfigBankCodeRequest loanConfigBankCodeRequest) {
        Optional<LoanConfiguration> loanConfigurationOptional =
                loanConfigurationRepository.findByBankCode(String.valueOf(loanConfigBankCodeRequest.getBankCode()));
        if (loanConfigurationOptional.isPresent()) {
            LoanConfiguration foundLoanConfig = loanConfigurationOptional.get();
            foundLoanConfig.setStatus(StatusConstants.DELETED);
            loanConfigurationRepository.save(foundLoanConfig);
        } else {
            System.out.println("Loan Configuration does not exist");
        }
    }

    }
