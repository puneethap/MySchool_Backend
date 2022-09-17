package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.UserOTP;

public interface LoginService {
    public String sendPasswordResetLinkViaMail(String emailId) throws Exception;
}
