package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.Messages;
import com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
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
        UserOtp user = findUserOtpByToken(token);
        if (StaticFieldsAndMethods.isTokenExpired(user.getTokenCreationDate())) {
            throw new ResourceNotFoundException(Messages.TOKEN_EXPIRED);
        }
        if (!(otp.equals(user.getOtp()))) {
            throw new ResourceNotFoundException(Messages.INVALID_OTP);
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
    public UserOtp findUserOtpByToken(String token) throws ResourceNotFoundException {
        Optional<UserOtp> userOtp = Optional.ofNullable(otpRepository.findByToken(token));
        if (!userOtp.isPresent()) {
            throw new ResourceNotFoundException(Messages.INVALID_TOKEN);
        }
        return userOtp.get();
    }

    @Override
    public String sendOtpViaMail(String emailId, String subject) throws Exception {
        User user = userService.findByEmail(emailId);

        UserOtp userOtp = new UserOtp(user.getId(), user.getEmail(), StaticFieldsAndMethods.generateToken(), LocalDateTime.now(), StaticFieldsAndMethods.generateOTP(4));

        emailService.sendSimpleMail(userOtp.getEmail(), subject, userOtp.getOtp());

        userOtp = saveUserOtp(userOtp);

        return userOtp.getToken();
    }
}
