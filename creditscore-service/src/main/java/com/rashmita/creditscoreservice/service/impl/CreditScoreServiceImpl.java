package com.rashmita.creditscoreservice.service.impl;
import com.rashmita.commoncommon.exception.NotFoundException;
import com.rashmita.commoncommon.model.CreditScoreByAccountNumber;
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

import java.util.List;

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
    public ServerResponse getCustomerByBankCodeAndAccountNumber(CreditScoreByAccountNumber request)
            throws NotFoundException {

        String bankCode = request.getBankCode();
        String accountNumber = request.getAccountNumber();

        List<CreditScore> creditScores = creditScoreRepository.findCustomerByBankCodeAndAccountNumber(bankCode, accountNumber);

        if (creditScores.isEmpty()) {
            throw new NotFoundException(
                    "Customer not found for bankCode: " + bankCode + " and accountNumber: " + accountNumber);
        }
        CreditScore first = creditScores.getFirst();
        CreditScoreResponse response = new CreditScoreResponse();
        response.setAccountNumber(first.getAccountNumber());
        response.setMobileNumber(first.getMobileNumber());
        response.setOneMonthLimit(first.getOneMonthLimit());
        response.setEmiMaxAmount(first.getEmiMaxAmount());
        List<CreditScoreResponse.Tenure> tenures = creditScores.stream()
                .map(cs -> new CreditScoreResponse.Tenure(cs.getEmiMonth(), cs.getEmiMaxAmount()))
                .toList();

        response.setTenures(tenures);

        return ResponseUtility.getSuccessfulServerResponse(response, "Customer found successfully");
    }

}
