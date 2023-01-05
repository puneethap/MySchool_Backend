package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.Messages;
import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserService userService;

    @Autowired
    OtpService otpService;

    @Override
    public String sendPasswordResetOtp(String email) throws Exception {
        return otpService.sendOtpViaMail(email, "Password Reset OTP");
    }

    @Override
    public String validatePasswordResetOtp(String token, String otp) throws ResourceNotFoundException {
        return otpService.validateOtp(token, otp);
    }

    @Override
    public String resetPassword(String token, String password) throws ResourceNotFoundException {
        Optional<UserOtp> optionalUserOtp = Optional.ofNullable(otpService.findUserOtpByToken(token));
        if (!optionalUserOtp.isPresent()) {
            throw new ResourceNotFoundException(Messages.INVALID_TOKEN);
        }
        UserOtp otp = optionalUserOtp.get();
        if (StaticFieldsAndMethods.isTokenExpired(otp.getTokenCreationDate())) {
            throw new ResourceNotFoundException(Messages.TOKEN_EXPIRED);
        }

        Optional<User> optionalUser = Optional.ofNullable(userService.findById(otp.getUserId()));
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND + " with id " + otp.getUserId());
        }

        User user = optionalUser.get();
        user.setPassword(password);

        userService.save(user);

        otpService.deleteUserOtp(otp);

        return Messages.PASSWORD_CHANGE_SUCCESS;
    }
}
