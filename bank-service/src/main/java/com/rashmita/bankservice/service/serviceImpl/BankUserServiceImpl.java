package com.rashmita.bankservice.service.serviceImpl;

import com.rashmita.bankservice.entity.BankUser;
import com.rashmita.bankservice.mapper.BankUserMapper;
import com.rashmita.bankservice.model.BankUserIdRequest;
import com.rashmita.bankservice.model.BankUserRequest;
import com.rashmita.bankservice.model.BankUserResponse;
import com.rashmita.bankservice.model.BankUserUpdateRequest;
import com.rashmita.bankservice.repository.BankUserRepository;
import com.rashmita.bankservice.service.BankUserService;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.model.PagingResponse;
import com.rashmita.common.model.ResponseUtility;
import com.rashmita.common.model.ServerResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankUserServiceImpl implements BankUserService {
    @Autowired
    private BankUserMapper bankUserMapper;
    @Autowired
    private BankUserRepository bankUserRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ServerResponse createBankUser(BankUserRequest bankUserRequestDto) throws NotFoundException {
        bankUserMapper.saveBankUserDetails(bankUserRequestDto);
        return ResponseUtility.getSuccessfulServerResponse("Bank User Created Successfully");
    }

    @Override
    public ServerResponse updateBankUser(BankUserUpdateRequest bankUserUpdateRequest) {
        bankUserMapper.updateBankUser(bankUserUpdateRequest);
       return ResponseUtility.getSuccessfulServerResponse("Bank User Updated Successfully");
    }

    @Override
    public ServerResponse deleteBankUser(BankUserIdRequest bankUserIdRequest) {
        bankUserMapper.deleteBankUser(bankUserIdRequest);
      return ResponseUtility.getSuccessfulServerResponse("BankUser Deleted Successfully");
    }

    @Override
    public ServerResponse getBankUserById(BankUserIdRequest bankUserIdRequest) throws NotFoundException {
        bankUserMapper.getDetailsById(bankUserIdRequest);
    return  ResponseUtility.getSuccessfulServerResponse("BankUser Details fetched Successfully");
    }

    @Override
    public ServerResponse getAllBankUsers(Pageable pageable) {
        Page<BankUser> bankUserPage = bankUserRepository.findAll(pageable);
        List<BankUserResponse> bankUserResponseDtos = bankUserPage.stream()
                .map(BankUser -> modelMapper.map(BankUser, BankUserResponse.class))
                .collect(Collectors.toList());

        return ResponseUtility.getSuccessfulServerResponse(
                new PagingResponse<>(
                        bankUserResponseDtos,
                        bankUserPage.getTotalPages(),
                        bankUserPage.getTotalElements(),
                        bankUserPage.getSize(),
                        bankUserPage.getNumber(),
                        bankUserPage.isEmpty()
                ), "All Bank User Found");
    }

    @Override
    public ServerResponse blockBankUser(BankUserIdRequest bankUserIdRequest) {
        bankUserMapper.blockBankUser(bankUserIdRequest);
        return ResponseUtility.getSuccessfulServerResponse("Bank USer Blocked Successfully");
    }

    @Override
    public ServerResponse unblockBankUser(BankUserIdRequest bankUserIdRequest) {
        bankUserMapper.unblockBankUser(bankUserIdRequest);
        return ResponseUtility.getSuccessfulServerResponse("Bank User unBlocked Successfully");
    }
}

