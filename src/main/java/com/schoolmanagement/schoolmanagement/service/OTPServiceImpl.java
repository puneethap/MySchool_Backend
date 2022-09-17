package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.UserOTP;
import com.schoolmanagement.schoolmanagement.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    OTPRepository otpRepository;
    @Override
    public UserOTP saveUserOTP(UserOTP userOTP) {
        return otpRepository.save(userOTP);
    }
}
