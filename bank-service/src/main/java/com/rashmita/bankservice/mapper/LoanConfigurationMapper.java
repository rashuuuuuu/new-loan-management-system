package com.rashmita.bankservice.mapper;

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
    public LoanConfiguration saveLoanConfiguration(LoanConfigurationRequest loanConfigurationRequest) {
        LoanConfiguration loanConfiguration = new LoanConfiguration();
        loanConfiguration.setMaximumLoanPeriod(loanConfigurationRequest.getMaximumLoanPeriod());
        loanConfiguration.setMaximumLoanPeriod(loanConfigurationRequest.getMaximumLoanPeriod());
        loanConfiguration.setLoanAdministrationFeeAmount(loanConfigurationRequest.getLoanAdministrationFeeAmount());
        loanConfiguration.setDefaultingPeriod(loanConfigurationRequest.getDefaultingPeriod());
        loanConfiguration.setMaximumLoanPeriod(loanConfigurationRequest.getMaximumLoanPeriod());
        loanConfiguration.setInterestRate(loanConfigurationRequest.getInterestRate());
        loanConfiguration.setModifiedDate(new Date());
        loanConfiguration.setStatus(StatusConstants.CREATED);
        loanConfiguration.setLateFeeCharge(loanConfigurationRequest.getLateFeeCharge());
        loanConfiguration.setLoanAdministrationFeeRate(loanConfigurationRequest.getLoanAdministrationFeeRate());
        loanConfiguration.setBank(bankRepository.getByBankCode(loanConfigurationRequest.getBankIdRequest().getBankCode()));
        return loanConfigurationRepository.save(loanConfiguration);
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
        public LoanConfigurationResponse getDetailsByBankCode(LoanConfigBankCodeRequest loanConfigBankCodeRequest) throws NotFoundException {
            Optional<LoanConfiguration> loanConfiguration = loanConfigurationRepository.findByBankCode(String.valueOf((loanConfigBankCodeRequest.getBankCode())));
            if (loanConfiguration.get().getStatus() == StatusConstants.DELETED) {
                throw new NotFoundException("Loan configuration is unavailable");
            }

            return modelMapper.map(loanConfiguration, LoanConfigurationResponse.class);
        }
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
