package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;

public interface OtpService {
    UserOtp saveUserOtp(UserOtp userOtp);

    String validateOtp(String token, String otp) throws ResourceNotFoundException;

    void deleteUserOtp(UserOtp otp);

    UserOtp findUserOtpByToken(String token) throws ResourceNotFoundException;

    String sendOtpViaMail(String emailId, String subject) throws Exception;
}
