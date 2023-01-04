package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.Erole;
import com.schoolmanagement.schoolmanagement.entity.Role;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.OtpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    @MockBean
    UserService userService;

    @MockBean
    EmailService emailService;

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
    void validateOtp_when_token_is_invalid() {
        when(otpRepository.findByToken(token)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> otpService.validateOtp(token, otp));
        assertEquals("Invalid Token", exception.getMessage());
    }

    @Test
    void validateOtp_when_token_expired() {
        userOtp = new UserOtp(1L, email, token, expiredTokenCreationDateTime, otp);

        when(otpRepository.findByToken(token)).thenReturn(userOtp);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> otpService.validateOtp(token, otp));
        assertEquals("Token Expired", exception.getMessage());
    }

    @Test
    void validateOtp_when_otp_is_invalid() {
        when(otpRepository.findByToken(token)).thenReturn(userOtp);

        String invalidOtp = String.valueOf(Integer.parseInt(otp) + 1);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> otpService.validateOtp(token, invalidOtp));
        assertEquals("Invalid OTP", exception.getMessage());
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
    void sendOtpViaMail_when_success() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, Erole.ROLE_ADMIN));
        User user = new User(1L, "username", email, "password", roles);

        when(userService.findByEmail(email)).thenReturn(user);
        doNothing().when(emailService).sendSimpleMail(any(), any(), any());
        when(otpRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        otpService.sendOtpViaMail(email, "Testing send OTP");

        verify(emailService, times(1)).sendSimpleMail(any(), any(), any());
        verify(otpRepository, times(1)).save(any());
    }

    @Test
    void sendOtpViaMail_when_no_account_found_with_given_email() {
        when(userService.findByEmail(email)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> otpService.sendOtpViaMail(email, "Testing send OTP"));
        assertEquals("No account found with this email", exception.getMessage());
    }
}