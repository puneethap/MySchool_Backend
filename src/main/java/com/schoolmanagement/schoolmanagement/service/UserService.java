package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.entity.UserOTP;

public interface UserService {
    public User findByUsername(String userName);

    public User findByEmail(String emailId);
}
