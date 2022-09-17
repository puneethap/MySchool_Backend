package com.schoolmanagement.schoolmanagement.controller;


import com.schoolmanagement.schoolmanagement.entity.UserOTP;
import com.schoolmanagement.schoolmanagement.model.JwtResponse;
import com.schoolmanagement.schoolmanagement.model.LoginRequest;
import com.schoolmanagement.schoolmanagement.securityConfig.jwt.JwtUtils;
import com.schoolmanagement.schoolmanagement.securityConfig.services.UserDetailsImpl;
import com.schoolmanagement.schoolmanagement.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mySchool")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("emailId") String emailId) throws Exception {
        return loginService.sendPasswordResetLinkViaMail(emailId);
    }

    @PostMapping("/validateOTP")
    public String validateOTP(@RequestBody UserOTP userOTP) {
        return "";
    }
}
