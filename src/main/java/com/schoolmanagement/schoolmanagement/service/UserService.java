package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;

public interface UserService {
    User findByUsername(String userName) throws ResourceNotFoundException;

    User findByEmail(String emailId) throws ResourceNotFoundException;

    User findById(Long id) throws ResourceNotFoundException;

    User save(User user);
}
