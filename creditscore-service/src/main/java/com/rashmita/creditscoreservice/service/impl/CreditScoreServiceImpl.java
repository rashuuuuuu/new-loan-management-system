package com.rashmita.creditscoreservice.service.impl;
import com.rashmita.commoncommon.exception.NotFoundException;
import com.rashmita.commoncommon.model.CreditScoreResponse;
import com.rashmita.commoncommon.model.ResponseUtility;
import com.rashmita.commoncommon.model.ServerResponse;
import com.rashmita.creditscoreservice.entity.CreditScore;
import com.rashmita.creditscoreservice.mapper.CreditScoreMapper;
import com.rashmita.creditscoreservice.model.CreditScoreRequest;
import com.rashmita.creditscoreservice.repository.CreditScoreRepository;
import com.rashmita.creditscoreservice.service.CreditScoreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CreditScoreServiceImpl implements CreditScoreService {
    @Autowired
    private final CreditScoreMapper creditScoreMapper;
    @Autowired
    private CreditScoreRepository creditScoreRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CreditScoreServiceImpl(CreditScoreMapper creditScoreMapper) {
        this.creditScoreMapper = creditScoreMapper;
    }

    @Override
    public ServerResponse<?> getCreditScoreByAccountNumber(CreditScoreRequest creditScoreRequest) throws NotFoundException {
        return ResponseUtility.getSuccessfulServerResponse(
                creditScoreMapper.getByCustomerNumber(creditScoreRequest),
                "Presenting Credit  Score"
        );
    }

    @Override
    public ServerResponse getCustomerByBankCodeAndAccountNumber(String bankCode, String customerNumber)
            throws NotFoundException {

        CreditScore creditScore = creditScoreRepository
                .findCustomerByBankCodeAndAccountNumber(bankCode, customerNumber)
                .orElseThrow(() -> new NotFoundException("Customer not found for bankCode: " + bankCode + " and customerNumber: " + customerNumber));
        CreditScoreResponse response = modelMapper.map(creditScore, CreditScoreResponse.class);
        return ResponseUtility.getSuccessfulServerResponse(response, "Customer found successfully");
    }

}
