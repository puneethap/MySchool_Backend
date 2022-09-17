package com.schoolmanagement.schoolmanagement.constant;

import java.util.Random;
import java.util.UUID;

public class StaticFieldsAndMethods {
    public static final String numberForOTP = "1234567890";

    public static String generateOTP(int otpLength) {
        Random random = new Random();
        char[] otp = new char[otpLength];
        for(int i = 0; i < otpLength; i++) {
            otp[i] = StaticFieldsAndMethods.numberForOTP.charAt(random.nextInt(StaticFieldsAndMethods.numberForOTP.length()));
        }

        return String.valueOf(otp);
    }

}
