package com.schoolmanagement.schoolmanagement.constant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class StaticFieldsAndMethods {
    private static final String NUMBER_FOR_OTP = "1234567890";
    private static final int EXPIRE_TOKEN_AFTER_MINUTES = 5;

    public static String generateOTP(int otpLength) {
        Random random = new Random();
        char[] otp = new char[otpLength];
        for (int i = 0; i < otpLength; i++) {
            otp[i] = NUMBER_FOR_OTP.charAt(random.nextInt(NUMBER_FOR_OTP.length()));
        }

        return String.valueOf(otp);
    }

    public static String generateToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString()).append(UUID.randomUUID().toString()).toString();
    }

    public static boolean isTokenExpired(LocalDateTime tokenCreationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration difference = Duration.between(tokenCreationDate, now);

        return difference.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }

}
