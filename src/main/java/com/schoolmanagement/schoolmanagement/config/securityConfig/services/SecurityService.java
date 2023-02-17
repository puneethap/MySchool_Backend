package com.schoolmanagement.schoolmanagement.config.securityConfig.services;

import com.schoolmanagement.schoolmanagement.entity.Erole;
import com.schoolmanagement.schoolmanagement.entity.User;
import com.schoolmanagement.schoolmanagement.model.CurrentUser;
import com.schoolmanagement.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class SecurityService {

    @Autowired
    private UserService userService;

    public CurrentUser currentUser() {
        final String userName = ofNullable(this.userName()).orElse("UNKNOWN");
        User user = this.userService.findByUsername(userName);
        CurrentUser currentUser = new CurrentUser();
        currentUser.setUserId(user.getId());
        currentUser.setUserName(user.getUsername());
        currentUser.setEmail(user.getEmail());
        currentUser.setRole(this.userRoles());
        return currentUser;
    }

    public List<Erole> userRoles() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> contextRoles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return contextRoles
                .stream()
                .map(Object::toString)
                .map(Erole::valueOf)
                .collect(Collectors.toList());
    }

    public String userName() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
