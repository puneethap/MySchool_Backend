package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    OtpRepository otpRepository;

    @Override
    public UserOtp saveUserOtp(UserOtp userOtp) {
        return otpRepository.save(userOtp);
    }

    @Override
    public String validateOtp(String token, String otp) throws ResourceNotFoundException {
        Optional<UserOtp> optionalUserOtp = Optional.ofNullable(findByToken(token));
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
    public void delete(UserOtp otp) {
        otpRepository.delete(otp);
    }

    @Override
    public UserOtp findByToken(String token) {
        return otpRepository.findByToken(token);
    }
}
