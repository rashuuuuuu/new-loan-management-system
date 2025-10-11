package com.rashmita.commoncommon.util;


import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp(int length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // generates 0–9
        }
        return otp.toString();
    }

    public static void main(String[] args) {
        System.out.println("Your OTP is: " + generateOtp(6));
    }
}