package com.rashmita.bankservice.mapper;
import com.rashmita.bankservice.entity.BankUser;
import com.rashmita.bankservice.model.BankUserIdRequest;
import com.rashmita.bankservice.model.BankUserRequest;
import com.rashmita.bankservice.model.BankUserResponse;
import com.rashmita.bankservice.model.BankUserUpdateRequest;
import com.rashmita.bankservice.repository.BankUserRepository;
import com.rashmita.common.constants.StatusConstants;
import com.rashmita.common.entity.AccessGroup;
import com.rashmita.common.entity.Status;
import com.rashmita.common.entity.User;
import com.rashmita.common.exception.NotFoundException;
import com.rashmita.common.repository.AccessGroupRepository;
import com.rashmita.common.repository.StatusRepository;
import com.rashmita.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankUserMapper {
    /**
     * rashmita subedi
     */
    @Autowired
    private final ModelMapper modelMapper;
    private final BankUserRepository bankUserRepository;
    private final AccessGroupRepository accessGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    @Transactional
    public BankUser saveBankUserDetails(BankUserRequest bankUserRequestRequest) throws NotFoundException {
        String accessGroupName = bankUserRequestRequest.getAccessGroup().getName();

        AccessGroup accessGroup = accessGroupRepository.findByName(accessGroupName)
                .orElseThrow(() -> new NotFoundException("Access group not found: " + accessGroupName));

        if (!"BANK_USER".equalsIgnoreCase(accessGroup.getName())) {
            throw new NotFoundException("Invalid access group name: " + accessGroupName);
        }
        BankUser bankUser = new BankUser();
        User user=new User();
        bankUser.setCode(UUID.randomUUID().toString());
        bankUser.setPassword(passwordEncoder.encode(bankUserRequestRequest.getPassword()));
        bankUser.setEmail(bankUserRequestRequest.getEmail());
        bankUser.setUsername(bankUserRequestRequest.getEmail());
        bankUser.setMobile(bankUserRequestRequest.getMobile());
        bankUser.setFirstName(bankUserRequestRequest.getFirstName());
        bankUser.setLastName(bankUserRequestRequest.getLastName());
        bankUser.setGender(bankUserRequestRequest.getGender());
        bankUser.setAccessGroup(accessGroup);
        user.setUsername(bankUserRequestRequest.getEmail());
        user.setPassword(passwordEncoder.encode(bankUserRequestRequest.getPassword()));
        user.setEmail(bankUserRequestRequest.getEmail());
        user.setAccessGroup(accessGroup);
        Status createdStatus = statusRepository.getStatusByName(StatusConstants.CREATED.getName());
        user.setStatus(createdStatus);
        userRepository.save(user);
        bankUser.setStatus(createdStatus);
        bankUser.setAccessGroup(accessGroupRepository.findByName(bankUserRequestRequest.getAccessGroup().getName()).orElseThrow());
        return bankUserRepository.save(bankUser);
    }

    public BankUser updateBankUser(BankUserUpdateRequest bankUserUpdateRequest) {

        String email = bankUserUpdateRequest.getEmail();
        BankUser bankUser = bankUserRepository.getBankUsersByEmail((email));
        if (bankUserUpdateRequest.getEmail() != null) {
            bankUser.setMobile(bankUserUpdateRequest.getMobile());
            bankUser.setFirstName(bankUserUpdateRequest.getFirstName());
            bankUser.setLastName(bankUserUpdateRequest.getLastName());
            bankUser.setGender(bankUserUpdateRequest.getGender());
            bankUser.setStatus(statusRepository.getStatusByName(StatusConstants.UPDATED.getName()));
        }
        return bankUserRepository.save(bankUser);
    }

    public BankUserResponse getDetailsById(BankUserIdRequest bankUserIdRequest) throws NotFoundException {

        BankUser bankUser = bankUserRepository.getBankUserDetailsById(bankUserIdRequest)
                .orElseThrow(() -> new NotFoundException("BankUser not found"));

        if (bankUser.getStatus() == statusRepository.getStatusByName(StatusConstants.DELETED.getName()) || bankUser.getEmail() == null) {
            throw new NotFoundException("BankUser is inactive or email");
        }

        return modelMapper.map(bankUser, BankUserResponse.class);
    }

//    public BankResponse getByEmail(BankNameRequest bankNameRequest) throws NotFoundException {
//        Bank bank = bankRepository.getBankDetailsByBankNameIgnoreCase(String.valueOf(bankNameRequest))
//                .orElseThrow(() -> new NotFoundException("Bank not found"));
//
//        if (bank.getStatus() == StatusConstants.DELETED || bank.getBankCode() == null) {
//            throw new NotFoundException("Bank is inactive or missing bank code");
//        }
//        return modelMapper.map(bank, BankResponse.class);
//    }

    public void deleteBankUser(BankUserIdRequest bankUserIdRequest) {
        Optional<BankUser> bankUserOptional = bankUserRepository.findById(bankUserIdRequest.getBankUserId());

        if (bankUserOptional.isPresent()) {
            BankUser foundBankUser = bankUserOptional.get();
            foundBankUser.setStatus(statusRepository.getStatusByName(StatusConstants.DELETED.getName()));
            bankUserRepository.save(foundBankUser);
        } else {
            System.out.println("Bank User does not exist");
        }
    }

    public void blockBankUser(BankUserIdRequest bankUserIdRequest) {
        Optional<BankUser> bankUserOptional = bankUserRepository.findById(bankUserIdRequest.getBankUserId());
        if (bankUserOptional.isPresent()) {
            if (bankUserOptional.get().getStatus() != statusRepository.getStatusByName(StatusConstants.DELETED.getName())) {
                BankUser foundBankUser = bankUserOptional.get();
                foundBankUser.setStatus(statusRepository.getStatusByName(StatusConstants.BLOCKED.getName()));
            }
        } else {
            System.out.println("Bank User does not exist");
        }
    }

    public void unblockBankUser(BankUserIdRequest bankUserIdRequest) {
        Optional<BankUser> bankUserOptional = bankUserRepository.findById(bankUserIdRequest.getBankUserId());
        if (bankUserOptional.isPresent()) {
            if (bankUserOptional.get().getStatus() == statusRepository.getStatusByName(StatusConstants.BLOCKED.getName())) {
                BankUser foundbankUser = bankUserOptional.get();
                foundbankUser.setStatus(statusRepository.getStatusByName(StatusConstants.UNBLOCKED.getName()));
            }
        } else {
            System.out.println("Only blocked Bank User can be unblocked");
        }
    }
}
