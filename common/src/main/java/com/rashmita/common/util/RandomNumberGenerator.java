package com.rashmita.common.util;

import java.util.Random;

public class RandomNumberGenerator {

    public static String generate10DigitRandomNumber() {
        Random random = new Random();
        // Generate a number between 1000000000 and 9999999999
        long number = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(number); // Convert long to String
    }

}