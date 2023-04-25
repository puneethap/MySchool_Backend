package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;

public interface UserService {
    public User findByUsername(String userName);

    public User findByEmail(String emailId) throws ResourceNotFoundException;

    public User findById(Long id) throws ResourceNotFoundException;

    public User save(User user);
}
