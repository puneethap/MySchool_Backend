package com.schoolmanagement.schoolmanagement.customAnnotations.classes;

import com.schoolmanagement.schoolmanagement.customAnnotations.interfaces.ValidatedOtp;
import com.schoolmanagement.schoolmanagement.entity.UserOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatedOtpValidator implements ConstraintValidator<ValidatedOtp, String> {

    @Autowired
    OtpService otpService;

    @Override
    public boolean isValid(String token, ConstraintValidatorContext constraintValidatorContext) {

        UserOtp userOtp = null;
        try {
            userOtp = otpService.findUserOtpByToken(token);
        } catch (ResourceNotFoundException e) {
            return false;
        }

        if (validatedOtp(userOtp)) {
            return true;
        }
        return false;
    }

    private boolean validatedOtp(UserOtp userOtp) {
        return userOtp.getOtpValidated() != null && userOtp.getOtpValidated().equals("Y");
    }
}
