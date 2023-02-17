package com.schoolmanagement.schoolmanagement.config.securityConfig.services;

import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("---loadUserByUsername called.---");
        User user = userRepository.findByUsername(username);
        if (null != user && user.getUsername().equals(username)) {
            return UserDetailsImpl.build(user);
        } else {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
    }
}
