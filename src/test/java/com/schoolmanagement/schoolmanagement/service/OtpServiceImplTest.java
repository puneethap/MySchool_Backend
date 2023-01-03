package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.OtpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OtpServiceImplTest {

    @MockBean
    OtpRepository otpRepository;

    @Autowired
    OtpService otpService;

    private UserOtp userOtp;

    private String token;

    private String otp;
    private LocalDateTime validTokenCreationDateTime;

    private LocalDateTime expiredTokenCreationDateTime;

    private String email;

    @BeforeEach
    void setup() {
        token = UUID.randomUUID().toString();
        otp = "1234";
        email = "abc@xyz.com";
        validTokenCreationDateTime = LocalDateTime.now();
        expiredTokenCreationDateTime = LocalDateTime.now().minusMinutes(StaticFieldsAndMethods.EXPIRE_TOKEN_AFTER_MINUTES);
        userOtp = new UserOtp(1L, email, token, validTokenCreationDateTime, otp);
    }

    @Test
    void saveUserOtp() {
        when(otpRepository.save(userOtp)).thenReturn(userOtp);

        UserOtp savedUserOtp = otpService.saveUserOtp(userOtp);

        assertEquals(userOtp, savedUserOtp);
    }

    @Test
    void validateOtp_when_success() throws ResourceNotFoundException {
        when(otpRepository.findByToken(token)).thenReturn(userOtp);
        when(otpRepository.save(userOtp)).thenReturn(userOtp);

        String returnedToken = otpService.validateOtp(token, otp);

        assertEquals(userOtp.getToken(), returnedToken);
    }

    @Test
    void validateOtp_when_token_not_present() {
        when(otpRepository.findByToken(token)).thenReturn(null);
        when(otpRepository.save(userOtp)).thenReturn(userOtp);

        assertThrows(ResourceNotFoundException.class, () -> otpService.validateOtp(token, otp), "Token Not present");
    }

    @Test
    void validateOtp_when_token_expired() {
        userOtp = new UserOtp(1L, email, token, expiredTokenCreationDateTime, otp);

        when(otpRepository.findByToken(token)).thenReturn(userOtp);
        when(otpRepository.save(userOtp)).thenReturn(userOtp);

        assertThrows(ResourceNotFoundException.class, () -> otpService.validateOtp(token, otp), "Token Expired");
    }

    @Test
    void validateOtp_when_otp_is_invalid() {
        when(otpRepository.findByToken(token)).thenReturn(userOtp);
        when(otpRepository.save(userOtp)).thenReturn(userOtp);

        String invalidOtp = String.valueOf(Integer.parseInt(otp) + 1);

        assertThrows(ResourceNotFoundException.class, () -> otpService.validateOtp(token, invalidOtp), "Invalid OTP");
    }

    @Test
    void deleteUserOtp() {
        doNothing().when(otpRepository).delete(any());

        otpService.deleteUserOtp(userOtp);

        verify(otpRepository, times(1)).delete(userOtp);
    }

    @Test
    void findUserOtpByToken_when_record_found() {
        when(otpRepository.findByToken(token)).thenReturn(userOtp);

        UserOtp fetchedUserOtp = otpService.findUserOtpByToken(token);

        assertEquals(userOtp, fetchedUserOtp);
    }

    @Test
    void findUserOtpByToken_when_record_not_found() {
        when(otpRepository.findByToken(token)).thenReturn(null);

        UserOtp fetchedUserOtp = otpService.findUserOtpByToken(token);

        assertEquals(null, fetchedUserOtp);
    }

    @Test
    void sendOtpViaMail() {
    }
}