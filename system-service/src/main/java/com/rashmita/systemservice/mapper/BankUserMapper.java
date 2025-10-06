package com.rashmita.systemservice.mapper;

import com.rashmita.systemservice.constants.StatusConstants;
import com.rashmita.systemservice.entity.BankUser;
import com.rashmita.systemservice.entity.Roles;
import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.model.BankUserIdRequest;
import com.rashmita.systemservice.model.BankUserRequest;
import com.rashmita.systemservice.model.BankUserResponse;
import com.rashmita.systemservice.model.BankUserUpdateRequest;
import com.rashmita.systemservice.repository.BankUserRepository;
import com.rashmita.systemservice.repository.StatusRepository;
import com.rashmita.systemservice.constants.RoleEnum;
import com.rashmita.systemservice.entity.User;
import com.rashmita.systemservice.repository.RoleRepository;
import com.rashmita.systemservice.repository.UserRepository;
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
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatusRepository statusRepository;

    @Transactional
    public BankUser saveBankUserDetails(BankUserRequest bankUserRequestRequest) {
        String userRole = bankUserRequestRequest.getRole().toUpperCase();

// 1 Check if the role exists in DB, else create it
        Roles role = roleRepository.findByName(RoleEnum.valueOf(userRole))
                .orElseGet(() -> {
                    Roles newRole = new Roles();
                    newRole.setName(RoleEnum.valueOf(userRole));
                    newRole.setDescription(RoleEnum.valueOf(userRole).name());
                    return roleRepository.save(newRole);
                });

        BankUser bankUser = new BankUser();
        bankUser.setCode(UUID.randomUUID().toString());
        bankUser.setPassword(passwordEncoder.encode(bankUserRequestRequest.getPassword()));
        bankUser.setEmail(bankUserRequestRequest.getEmail());
        bankUser.setMobile(bankUserRequestRequest.getMobile());
        bankUser.setFirstName(bankUserRequestRequest.getFirstName());
        bankUser.setLastName(bankUserRequestRequest.getLastName());
        bankUser.setGender(bankUserRequestRequest.getGender());
        bankUser.setStatus(statusRepository.findByName(StatusConstants.CREATED.getName()));
        bankUser.setRole(role);

        User user = new User();
        user.setEmail(bankUserRequestRequest.getEmail());
        user.setPassword(passwordEncoder.encode(bankUserRequestRequest.getPassword()));
        user.setRole(role);
        user.setStatus(statusRepository.findByName(StatusConstants.CREATED.getName()));
        user.setFullName(bankUserRequestRequest.getFirstName() + " " + bankUserRequestRequest.getLastName());
        userRepository.save(user);
        return bankUserRepository.save(bankUser);
    }

    public BankUser updateBankUser(BankUserUpdateRequest bankUserUpdateRequest) {

        String email = bankUserUpdateRequest.getEmail();
        BankUser bankUser = bankUserRepository.getByEmail(email);
        if (bankUserUpdateRequest.getEmail() != null) {
            bankUser.setMobile(bankUserUpdateRequest.getMobile());
            bankUser.setFirstName(bankUserUpdateRequest.getFirstName());
            bankUser.setLastName(bankUserUpdateRequest.getLastName());
            bankUser.setGender(bankUserUpdateRequest.getGender());
            bankUser.setStatus(statusRepository.findByName(StatusConstants.UPDATED.getName()));
        }
        return bankUserRepository.save(bankUser);
    }

    public BankUserResponse getDetailsById(BankUserIdRequest bankUserIdRequest) throws NotFoundException {

        BankUser bankUser = bankUserRepository.getBankUserDetailsById(bankUserIdRequest)
                .orElseThrow(() -> new NotFoundException("BankUser not found"));

        if (bankUser.getStatus() == statusRepository.findByName(StatusConstants.DELETED.getName())|| bankUser.getEmail() == null) {
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
            foundBankUser.setStatus(statusRepository.findByName(StatusConstants.DELETED.getName()));
            bankUserRepository.save(foundBankUser);
        } else {
            System.out.println("Bank User does not exist");
        }
    }

    public void blockBankUser(BankUserIdRequest bankUserIdRequest) {
        Optional<BankUser> bankUserOptional = bankUserRepository.findById(bankUserIdRequest.getBankUserId());
        if (bankUserOptional.isPresent()) {
            if (bankUserOptional.get().getStatus() != statusRepository.findByName(StatusConstants.DELETED.getName())) {
                BankUser foundBankUser = bankUserOptional.get();
                foundBankUser.setStatus(statusRepository.findByName(StatusConstants.BLOCKED.getName()));
            }
        } else {
            System.out.println("Bank User does not exist");
        }
    }

    public void unblockBankUser(BankUserIdRequest bankUserIdRequest) {
        Optional<BankUser> bankUserOptional = bankUserRepository.findById(bankUserIdRequest.getBankUserId());
        if (bankUserOptional.isPresent()) {
            if (bankUserOptional.get().getStatus() == statusRepository.findByName(StatusConstants.BLOCKED.getName())) {
                BankUser foundbankUser = bankUserOptional.get();
                foundbankUser.setStatus(statusRepository.findByName(StatusConstants.UNBLOCKED.getName()));
            }
        } else {
            System.out.println("Only blocked Bank User can be unblocked");
        }
    }
}
