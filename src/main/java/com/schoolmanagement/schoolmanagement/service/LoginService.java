package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;

public interface LoginService {

    public String resetPassword(String token, String password) throws ResourceNotFoundException;

    public String sendPasswordResetOtp(String email) throws Exception;

    public String validatePasswordResetOtp(String token, String otp) throws ResourceNotFoundException;
}
