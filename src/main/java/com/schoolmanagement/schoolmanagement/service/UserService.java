package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.User;

public interface UserService {
    public User findByUsername(String userName);
}
