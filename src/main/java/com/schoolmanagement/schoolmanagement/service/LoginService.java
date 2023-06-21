package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;

public interface LoginService {

    String resetPassword(String token, String password) throws ResourceNotFoundException;

    String sendPasswordResetOtp(String email) throws Exception;

    String validatePasswordResetOtp(String token, String otp) throws ResourceNotFoundException;
}
