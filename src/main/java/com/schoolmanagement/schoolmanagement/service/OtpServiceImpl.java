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
public class OtpServiceImpl implements OtpService {

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Override
    public UserOtp saveUserOtp(UserOtp userOtp) {
        return otpRepository.save(userOtp);
    }

    @Override
    public String validateOtp(String token, String otp) throws ResourceNotFoundException {
        Optional<UserOtp> optionalUserOtp = Optional.ofNullable(findUserOtpByToken(token));
        if (!optionalUserOtp.isPresent()) {
            throw new ResourceNotFoundException("Token Not present");
        }
        UserOtp user = optionalUserOtp.get();
        if (StaticFieldsAndMethods.isTokenExpired(user.getTokenCreationDate())) {
            throw new ResourceNotFoundException("Token Expired");
        }
        if (!(otp.equals(user.getOtp()))) {
            throw new ResourceNotFoundException("Invalid OTP");
        }

        user.setOtpValidated("Y");
        saveUserOtp(user);

        return user.getToken();
    }

    @Override
    public void deleteUserOtp(UserOtp otp) {
        otpRepository.delete(otp);
    }

    @Override
    public UserOtp findUserOtpByToken(String token) {
        return otpRepository.findByToken(token);
    }

    @Override
    public String sendPasswordResetOtpViaMail(String emailId) throws Exception {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByEmail(emailId));
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("No account found with this email");
        }

        User user = optionalUser.get();
        UserOtp userOtp = new UserOtp(user.getId(), user.getEmail(), StaticFieldsAndMethods.generateToken(), LocalDateTime.now(), StaticFieldsAndMethods.generateOTP(4));

        emailService.sendSimpleMail(userOtp.getEmail(), "OTP for password reset", userOtp.getOtp());

        userOtp = saveUserOtp(userOtp);

        return userOtp.getToken();
    }
}
