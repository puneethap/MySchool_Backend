package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.Messages;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String userName) throws ResourceNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userName));
        if (!user.isPresent()) {
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND + " with user name " + userName);
        }
        return user.get();
    }

    @Override
    public User findByEmail(String emailId) throws ResourceNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(emailId));
        if (!user.isPresent()) {
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND + " with email Id " + emailId);
        }
        return user.get();
    }

    @Override
    public User findById(Long id) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException(Messages.USER_NOT_FOUND + " with id " + id);
        }
        return user.get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
