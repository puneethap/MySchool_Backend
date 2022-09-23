package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    OtpService otpService;

    @Autowired
    OtpRepository otpRepository;

    @Override
    public String sendPasswordResetLinkViaMail(String emailId) throws Exception {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByEmail(emailId));
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("No account found with this email");
        }

        User user = optionalUser.get();
        UserOtp userOtp = new UserOtp(user.getId(), user.getEmail(), StaticFieldsAndMethods.generateToken(), LocalDateTime.now(), StaticFieldsAndMethods.generateOTP(4));

        emailService.sendSimpleMail(userOtp.getEmail(), "OTP for password reset", userOtp.getOtp());
        userOtp = otpService.saveUserOtp(userOtp);
        return userOtp.getToken();
    }

    @Override
    public String resetPassword(String token, String password) throws ResourceNotFoundException {
        Optional<UserOtp> optionalUserOtp = Optional.ofNullable(otpRepository.findByToken(token));
        if (!optionalUserOtp.isPresent()) {
            throw new ResourceNotFoundException("Token Not present");
        }
        UserOtp otp = optionalUserOtp.get();
        if (StaticFieldsAndMethods.isTokenExpired(otp.getTokenCreationDate())) {
            throw new ResourceNotFoundException("Token Expired");
        }

        User user = userService.findById(otp.getUserId());
        user.setPassword(password);

        userService.save(user);

        otpService.delete(otp);

        return "Password changed successfully";
    }

}
