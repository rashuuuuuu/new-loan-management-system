package com.rashmita.systemservice.mapper;

import com.rashmita.systemservice.constants.StatusConstants;
import com.rashmita.systemservice.entity.Bank;
import com.rashmita.systemservice.entity.BankAdmin;
import com.rashmita.systemservice.entity.Status;
import com.rashmita.systemservice.entity.User;
import com.rashmita.systemservice.exception.NotFoundException;
import com.rashmita.systemservice.model.*;
import com.rashmita.systemservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankMapper {

    private final BankRepository bankRepository;
    private final BankAdminRepository bankAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatusRepository statusRepository;
    private final AccessGroupRepository accessGroupRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    /**
     * Only SuperAdmin creates a bank.
     */

    @Transactional
    public Bank saveBankDetails(BankRequest bankRequest) throws NotFoundException {
        log.info("Saving new bank details for bankCode={}", bankRequest.getBankCode());

        var accessGroupName = bankRequest.getAccessGroup().getName();
        var accessGroup = accessGroupRepository.findByName(accessGroupName)
                .orElseThrow(() -> new NotFoundException("Access group not found: " + accessGroupName));

        BankAdmin bankAdmin = new BankAdmin();
        bankAdmin.setEmail(bankRequest.getBankAdminRequest().getEmail());
        bankAdmin.setPassword(passwordEncoder.encode(bankRequest.getBankAdminRequest().getPassword()));
        bankAdmin.setAccessGroup(accessGroup);
        Status createdStatus = statusRepository.getStatusByName(StatusConstants.CREATED.getName());
        bankAdmin.setStatus(createdStatus);
        User user = new User();
        user.setEmail(bankRequest.getBankAdminRequest().getEmail());
        user.setPassword(passwordEncoder.encode(bankRequest.getBankAdminRequest().getPassword()));
        user.setUsername(bankRequest.getBankAdminRequest().getEmail());
        user.setStatus(createdStatus);
        user.setAccessGroup(accessGroup);
        Bank bank = new Bank();
        bank.setBankCode(bankRequest.getBankCode());
        bank.setAddress(bankRequest.getBankAddress());
        bank.setEstablishedDate(bankRequest.getEstablishedDate());
        bank.setStatus(createdStatus);
        bank.setBankAdmin(bankAdmin);
        bankAdmin.setBank(bank);
        userRepository.save(user);
        bankRepository.save(bank);
        log.info(" Bank created successfully with code {}", bank.getBankCode());
        return bank;
    }

    public Bank updateBankDetails(BankUpdateRequest bankUpdateRequest) throws NotFoundException {
        Bank bank = bankRepository.getByBankCode(bankUpdateRequest.getBankCode());
        if (bank == null) {
            throw new NotFoundException("Bank not found for code: " + bankUpdateRequest.getBankCode());
        }

        if (bankUpdateRequest.getIsActive() != null) {
            bank.setIsActive(bankUpdateRequest.getIsActive());
        }

        return bankRepository.save(bank);
    }

    public BankResponse getDetailsById(BankIdRequest bankId) throws NotFoundException {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new NotFoundException("Bank not found with ID: " + bankId));

        if (bank.getStatus().getName().equals(StatusConstants.DELETED.getName())) {
            throw new NotFoundException("Bank is inactive or deleted");
        }

        return modelMapper.map(bank, BankResponse.class);
    }

    @Transactional
    public void deleteBank(BankCodeRequest bankCode) throws NotFoundException {
        Optional<Bank> bankOpt = bankRepository.findByBankCode(bankCode.getBankCode());
        if (bankOpt.isEmpty()) throw new NotFoundException("Bank not found for code: " + bankCode);

        Bank bank = bankOpt.get();
        BankAdmin admin = bankAdminRepository.findByBank(bank)
                .orElseThrow(() -> new NotFoundException("Bank admin not found for bank: " + bankCode));

        Status deletedStatus = statusRepository.getStatusByName(StatusConstants.DELETED.getName());
        bank.setStatus(deletedStatus);
        admin.setStatus(deletedStatus);

        bankRepository.save(bank);
        bankAdminRepository.save(admin);

        log.info("Bank deleted successfully for code {}", bankCode);
    }
    public BankResponse getByBankName(BankCodeRequest bankCodeRequest) throws NotFoundException {
        Bank bank = bankRepository.getByBankCode(String.valueOf(bankCodeRequest));
        if (bank.getStatus() == statusRepository.getStatusByName(StatusConstants.DELETED.getName()) || bank.getBankCode() == null) {
            throw new NotFoundException("Bank is inactive or missing bank code"); } return modelMapper.map(bank, BankResponse.class); }
}
