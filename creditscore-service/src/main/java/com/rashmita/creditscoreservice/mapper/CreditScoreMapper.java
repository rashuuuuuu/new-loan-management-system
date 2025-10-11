package com.rashmita.creditscoreservice.mapper;
import com.rashmita.commoncommon.exception.NotFoundException;
import com.rashmita.creditscoreservice.entity.CreditScore;
import com.rashmita.creditscoreservice.model.CreditScoreRequest;
import com.rashmita.creditscoreservice.model.CreditScoreResponse;
import com.rashmita.creditscoreservice.repository.CreditScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditScoreMapper {
    private final CreditScoreRepository creditScoreRepository;
    private final ModelMapper modelMapper;

    // Return a list of responses instead of a single response
    public List<CreditScoreResponse> getByCustomerNumber(CreditScoreRequest creditScoreRequest) throws NotFoundException {
        List<CreditScore> creditScores = creditScoreRepository
                .getCreditScoreByAccountNumber(creditScoreRequest.getAccountNumber());

        if (creditScores.isEmpty()) {
            throw new NotFoundException("credit score not found");
        }

        // Map all entities to response objects
        return creditScores.stream()
                .map(cs -> modelMapper.map(cs, CreditScoreResponse.class))
                .collect(Collectors.toList());
    }


}