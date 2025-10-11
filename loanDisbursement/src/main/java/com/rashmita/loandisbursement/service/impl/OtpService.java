//package com.rashmita.loandisbursement.service.impl;
//
//
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.security.SecureRandom;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class OtpService {
//
//    private static final SecureRandom random = new SecureRandom();
//    private final StringRedisTemplate redisTemplate;
//
//    public OtpService(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    /**
//     * Generate and cache OTP for userId with 5 min expiry
//     */
//    public String generateOtp(String userId) {
//        String otp = String.format("%06d", random.nextInt(999999));
//        String key = "OTP_" + userId;
//
//        // store with expiry of 5 minutes
//        redisTemplate.opsForValue().set(key, otp, 5, TimeUnit.MINUTES);
//        return otp;
//    }
//
//    /**
//     * Validate OTP and delete it after successful validation
//     */
//    public boolean validateOtp(String userId, String inputOtp) {
//        String key = "OTP_" + userId;
//        String storedOtp = redisTemplate.opsForValue().get(key);
//
//        if (storedOtp != null && storedOtp.equals(inputOtp)) {
//            redisTemplate.delete(key); // one-time use
//            return true;
//        }
//        return false;
//    }
//}