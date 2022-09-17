package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.entity.UserOTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService{

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 5;
    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    OTPService otpService;
    @Override
    public String sendPasswordResetLinkViaMail(String emailId) throws Exception {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByEmail(emailId));
        if(!optionalUser.isPresent()) {
            return "Email not found";
        }

        User user = optionalUser.get();
        UserOTP userOTP = new UserOTP(user.getId(), user.getEmail(), generateToken(), LocalDateTime.now(), StaticFieldsAndMethods.generateOTP(4));

        emailService.sendSimpleMail(userOTP.getEmail(), "OTP for password reset", userOTP.getOTP());
        userOTP = otpService.saveUserOTP(userOTP);
        return userOTP.getToken();
    }

    private static String generateToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString()).append(UUID.randomUUID().toString()).toString();
    }

    private boolean isTokenExpired(LocalDateTime tokenCreationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration difference = Duration.between(tokenCreationDate, now);

        return difference.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }
}
