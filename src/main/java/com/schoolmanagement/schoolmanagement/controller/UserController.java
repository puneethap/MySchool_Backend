package com.schoolmanagement.schoolmanagement.controller;

import com.schoolmanagement.schoolmanagement.config.securityConfig.services.SecurityService;
import com.schoolmanagement.schoolmanagement.model.ApiError;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import com.schoolmanagement.schoolmanagement.model.CurrentUser;
import com.schoolmanagement.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/currentUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CurrentUser>> findByUserName() {
        try {
            return ok(new ApiResponse<>(this.securityService.currentUser()));
        } catch (final Exception e) {
            return ok(new ApiResponse<>(new ApiError(e.getMessage())));
        }
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }
}