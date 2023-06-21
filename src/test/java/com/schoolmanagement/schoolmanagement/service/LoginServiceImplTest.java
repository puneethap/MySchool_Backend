package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.Messages;
import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.Erole;
import com.schoolmanagement.schoolmanagement.entity.Role;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.schoolmanagement.schoolmanagement.constant.Messages.INVALID_TOKEN;
import static com.schoolmanagement.schoolmanagement.constant.Messages.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginServiceImplTest {

    private String email;

    private String token;

    private LocalDateTime tokenCreationDate;

    private String password;

    private String otp;

    private Long userId;

    @Autowired
    LoginService loginService;

    @MockBean
    OtpService otpService;

    @MockBean
    UserService userService;

    @BeforeEach
    void setup() {
        email = "abc@xyz.com";
        token = StaticFieldsAndMethods.generateToken();
        tokenCreationDate = LocalDateTime.now();
        otp = "1234";
        password = "password";
        userId = 1L;
    }

    @Test
    void sendPasswordResetOtp() throws Exception {
        when(otpService.sendOtpViaMail(any(), any())).thenReturn(token);

        String fetchedToken = loginService.sendPasswordResetOtp(email);

        assertEquals(token, fetchedToken);

    }

    @Test
    void validatePasswordResetOtp() throws ResourceNotFoundException {
        when(otpService.validateOtp(any(), any())).thenReturn(token);

        String fetchedToken = loginService.validatePasswordResetOtp(token, otp);

        assertEquals(token, fetchedToken);
    }

    @Test
    void resetPassword_when_success() throws ResourceNotFoundException {
        UserOtp userOtp = new UserOtp(userId, email, token, tokenCreationDate, otp);

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, Erole.ROLE_ADMIN));
        User user = new User(1L, "username", email, password, roles);

        when(otpService.findUserOtpByToken(token)).thenReturn(userOtp);
        when(userService.findById(userOtp.getUserId())).thenReturn(user);
        when(userService.save(any())).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(otpService).deleteUserOtp(any());

        String successMessage = loginService.resetPassword(token, password);

        assertEquals(Messages.PASSWORD_CHANGE_SUCCESS, successMessage);
    }

    @Test
    void resetPassword_when_token_is_invalid() throws ResourceNotFoundException {
        when(otpService.findUserOtpByToken(token)).thenThrow(new ResourceNotFoundException(INVALID_TOKEN));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> loginService.resetPassword(token, password));
        assertEquals(INVALID_TOKEN, exception.getMessage());
    }

    @Test
    void resetPassword_when_token_is_expired() throws ResourceNotFoundException {
        LocalDateTime expiredTokenCreationDate = tokenCreationDate.minusMinutes(StaticFieldsAndMethods.EXPIRE_TOKEN_AFTER_MINUTES);
        UserOtp userOtp = new UserOtp(userId, email, token, expiredTokenCreationDate, otp);

        when(otpService.findUserOtpByToken(token)).thenReturn(userOtp);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> loginService.resetPassword(token, password));
        assertEquals(Messages.TOKEN_EXPIRED, exception.getMessage());
    }

    @Test
    void resetPassword_when_user_is_not_present_in_user_table() throws ResourceNotFoundException {
        UserOtp userOtp = new UserOtp(userId, email, token, tokenCreationDate, otp);

        when(otpService.findUserOtpByToken(token)).thenReturn(userOtp);
        when(userService.findById(userOtp.getUserId())).thenThrow(new ResourceNotFoundException(USER_NOT_FOUND));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> loginService.resetPassword(token, password));
        assertTrue(exception.getMessage().contains(USER_NOT_FOUND));
    }
}