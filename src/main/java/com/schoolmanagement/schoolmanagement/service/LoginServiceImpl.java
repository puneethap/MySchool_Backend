package com.schoolmanagement.schoolmanagement.service;

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
    public String resetPassword(String token, String password) throws ResourceNotFoundException {
        Optional<UserOtp> optionalUserOtp = Optional.ofNullable(otpService.findUserOtpByToken(token));
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

        otpService.deleteUserOtp(otp);

        return "Password changed successfully";
    }

}
