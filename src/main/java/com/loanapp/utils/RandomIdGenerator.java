package com.loanapp.utils;

import java.security.SecureRandom;

public class RandomIdGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int LENGTH = 14;
    private static final String DIGITS = "0123456789";

    public static String generateRandomId() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = secureRandom.nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(index));
        }
        return sb.toString();
    }
}
