package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.User;

public interface UserService {
    public User findByUsername(String userName);

    public User findByEmail(String emailId);

    public User findById(Long id);

    public User save(User user);
}
