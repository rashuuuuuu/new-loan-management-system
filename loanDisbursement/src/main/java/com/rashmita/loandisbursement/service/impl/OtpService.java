package com.rashmita.loandisbursement.service.impl;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();
    public String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
