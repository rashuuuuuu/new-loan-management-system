//package com.rashmita.systemservice.service.impl;
//
//
//import com.rashmita.systemservice.mapper.LoanConfigurationMapper;
//import com.rashmita.systemservice.model.ResponseUtility;
//import com.rashmita.systemservice.model.ServerResponse;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class LoanConfigurationServiceImpl implements LoanConfigurationService {
//    @Autowired
//    private LoanConfigurationMapper loanConfigurationMapper;
//    @Autowired
//    private LoanConfigurationRepository loanConfigurationRepository;
//    @Autowired
//    private ModelMapper modelMapper;
//    @Override
//    public ServerResponse createLoanConfig(LoanConfigurationRequest loanConfigurationRequest) {
//        loanConfigurationMapper.saveLoanConfiguration(loanConfigurationRequest);
//        return ResponseUtility.getSuccessfulServerResponse("loan configuration Created Successfully");
//    }
//
//    @Override
//    public ServerResponse updateLoanConfig(LoanUpdateRequest loanUpdateRequest) {
//        loanConfigurationMapper.updateLoanConfiguration(loanUpdateRequest);
//        return ResponseUtility.getSuccessfulServerResponse("loan configuration Updated Successfully");
//    }
//
//    @Override
//    public ServerResponse deleteLoanConfig(LoanConfigIdRequest loanConfigIdRequest) {
//        loanConfigurationMapper.deleteLoanConfiguration(loanConfigIdRequest);
//        return ResponseUtility.getSuccessfulServerResponse("loan Configuration  Deleted Successfully");
//    }
//
//    @Override
//    public ServerResponse getLoanConfigById(LoanConfigIdRequest loanConfigIdRequest) throws NotFoundException {
//        loanConfigurationMapper.getDetailsById(loanConfigIdRequest);
//        return  ResponseUtility.getSuccessfulServerResponse("loan Configuration details  fetched Successfully");
//    }
//}
