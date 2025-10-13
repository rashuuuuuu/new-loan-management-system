package com.rashmita.bankservice.service.serviceImpl;
import com.rashmita.bankservice.mapper.LoanConfigurationMapper;
import com.rashmita.bankservice.model.LoanConfigBankCodeRequest;
import com.rashmita.bankservice.model.LoanConfigurationRequest;
import com.rashmita.bankservice.model.LoanConfigurationResponse;
import com.rashmita.bankservice.model.LoanUpdateRequest;
import com.rashmita.bankservice.repository.LoanConfigurationRepository;
import com.rashmita.bankservice.service.LoanConfigurationService;
import com.rashmita.common.constants.StatusConstants;
import com.rashmita.common.entity.LoanConfiguration;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.ResponseUtility;
import com.rashmita.common.model.ServerResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanConfigurationServiceImpl implements LoanConfigurationService {
    @Autowired
    private LoanConfigurationMapper loanConfigurationMapper;
    private final LoanConfigurationRepository loanConfigurationRepository;
    private final ModelMapper modelMapper;

    public LoanConfigurationServiceImpl(LoanConfigurationRepository loanConfigurationRepository, ModelMapper modelMapper) {
        this.loanConfigurationRepository = loanConfigurationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ServerResponse<?> createLoanConfig(LoanConfigurationRequest loanConfigurationRequest) {
        loanConfigurationMapper.saveLoanConfiguration(loanConfigurationRequest);
        return ResponseUtility.getSuccessfulServerResponse("loan configuration Created Successfully");
    }

    @Override
    public ServerResponse<?> updateLoanConfig(LoanUpdateRequest loanUpdateRequest) {
        loanConfigurationMapper.updateLoanConfiguration(loanUpdateRequest);
        return ResponseUtility.getSuccessfulServerResponse("loan configuration Updated Successfully");
    }

    @Override
    public ServerResponse<?> deleteLoanConfig(LoanConfigBankCodeRequest loanConfigBankCodeRequest) {
        loanConfigurationMapper.deleteLoanConfiguration(loanConfigBankCodeRequest);
        return ResponseUtility.getSuccessfulServerResponse("loan Configuration  Deleted Successfully");
    }

    @Override
    public ServerResponse<LoanConfigurationResponse> getLoanConfigByBankCode(LoanConfigBankCodeRequest request)
            throws NotFoundException {
        LoanConfiguration loanConfig = loanConfigurationRepository.findByBankCode(request.getBankCode())
                .orElseThrow(() -> new NotFoundException(
                        "Loan configuration not found for bankCode: " + request.getBankCode()
                ));
        if (loanConfig.getStatus() == StatusConstants.DELETED) {
            throw new NotFoundException("Loan configuration is unavailable for bankCode: " + request.getBankCode());
        }
        LoanConfigurationResponse response = modelMapper.map(loanConfig, LoanConfigurationResponse.class);
        return ResponseUtility.getSuccessfulServerResponse(response, "Loan configuration fetched successfully");
    }
}
