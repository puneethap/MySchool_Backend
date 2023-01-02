package com.schoolmanagement.schoolmanagement.constant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StaticFieldsAndMethodsTest {

    private static final int EXPIRE_TOKEN_AFTER_MINUTES = 5;
    private static final int OTP_LENGTH = 4;

    @Test
    void testGenerateOTP() {
        String otp = StaticFieldsAndMethods.generateOTP(OTP_LENGTH);
        assertEquals(OTP_LENGTH, otp.length());
    }

    @Test
    void testIsTokenExpired_when_token_not_expired() {
        LocalDateTime tokenCreationDateTime = LocalDateTime.now();
        boolean isTokenExpired = StaticFieldsAndMethods.isTokenExpired(tokenCreationDateTime);

        assertFalse(isTokenExpired);
    }

    @Test
    void testIsTokenExpired_when_token_is_expired() {
        LocalDateTime tokenCreationDateTime = LocalDateTime.now().minusMinutes(EXPIRE_TOKEN_AFTER_MINUTES);
        boolean isTokenExpired = StaticFieldsAndMethods.isTokenExpired(tokenCreationDateTime);

        assertTrue(isTokenExpired);
    }
}