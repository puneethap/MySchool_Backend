package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;

public interface OtpService {
    public UserOtp saveUserOtp(UserOtp userOtp);

    public String validateOtp(String token, String otp) throws ResourceNotFoundException;

    public void delete(UserOtp otp);
}
