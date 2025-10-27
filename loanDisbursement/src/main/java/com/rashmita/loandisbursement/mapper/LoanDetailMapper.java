package com.rashmita.loandisbursement.mapper;

import com.rashmita.commoncommon.entity.LoanDetails;
import com.rashmita.commoncommon.model.LoanDetailResponse;
import com.rashmita.commoncommon.repository.LoanDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanDetailMapper {
    private final ModelMapper modelMapper;
    private final LoanDetailsRepository loanDetailsRepository;

    public LoanDetailMapper(ModelMapper modelMapper, LoanDetailsRepository loanDetailsRepository) {
        this.modelMapper = modelMapper;
        this.loanDetailsRepository = loanDetailsRepository;
    }

    public List<LoanDetailResponse> getAllLoanDetails() {
        List<LoanDetails> loanDetails = loanDetailsRepository.findAll();
        return loanDetails.stream()
                .map((detail) -> modelMapper.map(detail, LoanDetailResponse.class))
                .collect(Collectors.toList());
    }
}
