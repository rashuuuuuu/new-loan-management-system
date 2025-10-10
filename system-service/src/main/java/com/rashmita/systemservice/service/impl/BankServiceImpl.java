package com.rashmita.systemservice.service.impl;
import com.rashmita.common.entity.Bank;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.*;
import com.rashmita.common.repository.BankRepository;
import com.rashmita.systemservice.mapper.BankMapper;
import com.rashmita.systemservice.model.BankRequest;
import com.rashmita.systemservice.model.BankUpdateRequest;
import com.rashmita.systemservice.service.BankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl  implements BankService {
    @Autowired
    private final BankMapper bankMapper;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final BankRepository bankRepository;

    public BankServiceImpl(BankMapper bankMapper, ModelMapper modelMapper, BankRepository bankRepository) {
        this.bankMapper = bankMapper;
        this.modelMapper = modelMapper;
        this.bankRepository = bankRepository;
    }

    @Override
    public ServerResponse<?> createBank(BankRequest bankRequestDto) throws NotFoundException {
        bankMapper.saveBankDetails(bankRequestDto);
        return ResponseUtility.getSuccessfulServerResponse("Bank Created Successfully");
    }

    @Override
    public ServerResponse updateBank(BankUpdateRequest bankUpdateRequest) throws NotFoundException {
        bankMapper.updateBankDetails(bankUpdateRequest);
        return ResponseUtility.getSuccessfulServerResponse("Bank Updated Successfully");
    }

    @Override
    public ServerResponse deleteBank(BankCodeRequest bankCodeRequest) throws NotFoundException {
        bankMapper.deleteBank(bankCodeRequest);
        return ResponseUtility.getSuccessfulServerResponse("Bank Deleted Successfully");
    }

    @Override
    public ServerResponse getBankById(BankIdRequest bankIdRequest) throws NotFoundException {
        return ResponseUtility.getSuccessfulServerResponse(
                bankMapper.getDetailsById(bankIdRequest),
                "Presenting Bank Details"
        );
    }

    @Override
    public ServerResponse getAllBanks(Pageable pageable) {
        Page<Bank> bankPage = bankRepository.findAll(pageable);
        List<BankResponse> bankResponseDtos = bankPage.stream()
                .map(bank -> modelMapper.map(bank, BankResponse.class))
                .collect(Collectors.toList());

        return ResponseUtility.getSuccessfulServerResponse(
                new PagingResponse<>(
                        bankResponseDtos,
                        bankPage.getTotalPages(),
                        bankPage.getTotalElements(),
                        bankPage.getSize(),
                        bankPage.getNumber(),
                        bankPage.isEmpty()
                ), "All Banks Found");
    }

    @Override
    public ServerResponse getByBankCode(BankCodeRequest bankCodeRequest) throws NotFoundException {
        return ResponseUtility.getSuccessfulServerResponse(
                bankMapper.getByBankName(bankCodeRequest),
                "Presenting Bank Details"
        );
    }
}
