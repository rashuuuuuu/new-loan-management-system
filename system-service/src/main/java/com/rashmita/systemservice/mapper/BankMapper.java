package com.rashmita.systemservice.mapper;

import com.rashmita.systemservice.constants.StatusConstants;
import com.rashmita.systemservice.entity.Bank;
import com.rashmita.systemservice.entity.BankAdmin;
import com.rashmita.systemservice.entity.Roles;
import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.model.*;
import com.rashmita.systemservice.repository.BankAdminRepository;
import com.rashmita.systemservice.repository.BankRepository;
import com.rashmita.systemservice.repository.StatusRepository;
import com.rashmita.systemservice.constants.RoleEnum;
import com.rashmita.systemservice.entity.User;
import com.rashmita.systemservice.repository.RoleRepository;
import com.rashmita.systemservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankMapper {
    private final BankRepository bankRepository;
    /**
     * rashmita subedi
     */
    @Autowired
    private final ModelMapper modelMapper;
    private final BankAdminRepository bankAdminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    @Transactional
    public Bank saveBankDetails(BankRequest bankRequest) {
        Roles bankAdminRole = roleRepository.findByName(RoleEnum.BANKADMIN)
                .orElseThrow(() -> new RuntimeException("Role BANK_ADMIN not found"));

        // 1. Create and save user
        User user = new User();
        user.setFullName(bankRequest.getBankAdminRequest().getAdminName());
        user.setEmail(bankRequest.getBankAdminRequest().getEmail());
        user.setPassword(passwordEncoder.encode(bankRequest.getBankAdminRequest().getPassword()));
        user.setRole(bankAdminRole);
        user.setStatus(statusRepository.findByName(StatusConstants.CREATED.getName()));
        user = userRepository.save(user);

        // 2. Create BankAdmin
        BankAdmin bankAdmin = new BankAdmin();
        bankAdmin.setName(bankRequest.getBankAdminRequest().getAdminName());
        bankAdmin.setEmail(bankRequest.getBankAdminRequest().getEmail());
        bankAdmin.setPassword(passwordEncoder.encode(bankRequest.getBankAdminRequest().getPassword()));
        bankAdmin.setStatus(statusRepository.findByName(StatusConstants.CREATED.getName()));

        // 3. Create and save bank with bankAdmin
        Bank bank = new Bank();
        bank.setAddress(bankRequest.getBankAddress());
        bank.setBankCode(bankRequest.getBankCode());
        bank.setEstablishedDate(bankRequest.getEstablishedDate());
        bank.setStatus(statusRepository.findByName(StatusConstants.CREATED.getName()));
        bank.setBankAdmin(bankAdmin);
        bankAdmin.setBank(bank);
        bankAdminRepository.save(bankAdmin);// set before saving
        bank = bankRepository.save(bank);

        return bank;
    }

    public Bank updateBankDetails(BankUpdateRequest bankUpdateRequest) {

        String bankCode = bankUpdateRequest.getBankCode();
        Bank bank = bankRepository.getByBankCode(bankCode);
        if (bankUpdateRequest.getBankCode() != null) {
        bank.setIsActive(bankUpdateRequest.getIsActive());
        }
        return bankRepository.save(bank);
    }

    public BankResponse getDetailsById(BankIdRequest bankIdRequest) throws NotFoundException {

        Bank bank = bankRepository.getBankDetailsById(bankIdRequest)
                .orElseThrow(() -> new NotFoundException("Bank not found"));

        if (bank.getStatus() == statusRepository.findByName(StatusConstants.DELETED.getName()) || bank.getBankCode() == null) {
            throw new NotFoundException("Bank is inactive or missing bank code");
        }

        return modelMapper.map(bank, BankResponse.class);
    }

    public BankResponse getByBankName(BankCodeRequest bankCodeRequest) throws NotFoundException {
        Bank bank = bankRepository.getByBankCode(String.valueOf(bankCodeRequest));

        if (bank.getStatus() == statusRepository.findByName(StatusConstants.DELETED.getName()) || bank.getBankCode() == null) {
            throw new NotFoundException("Bank is inactive or missing bank code");
        }
        return modelMapper.map(bank, BankResponse.class);
    }

    public void deleteBank(BankCodeRequest bankCodeRequest) {
        Optional<Bank> bankOptional = bankRepository.findByBankCode(bankCodeRequest.getBankCode());
        Optional<BankAdmin> bankAdmin=bankAdminRepository.findByBank(bankOptional.get());

        if (bankOptional.isPresent()) {
            Bank foundBank = bankOptional.get();
            BankAdmin foundBankAdmin = bankAdmin.get();
            foundBankAdmin.setStatus(statusRepository.findByName(StatusConstants.DELETED.getName()));
            foundBank.setStatus(statusRepository.findByName(StatusConstants.DELETED.getName()));
            bankRepository.save(foundBank);
        } else {
            System.out.println("Bank does not exist");
        }
    }

}