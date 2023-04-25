package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.Messages;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User findByEmail(String emailId) throws ResourceNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(emailId));
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND + " with email Id " + emailId);
        }
        return optionalUser.get();
    }

    @Override
    public User findById(Long id) throws ResourceNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND + " with id " + id);
        }
        return optionalUser.get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
