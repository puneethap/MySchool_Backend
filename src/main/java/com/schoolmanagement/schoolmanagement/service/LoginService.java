package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;

public interface LoginService {
    public String sendPasswordResetOtpViaMail(String emailId) throws Exception;

    public String resetPassword(String token, String password) throws ResourceNotFoundException;
}
