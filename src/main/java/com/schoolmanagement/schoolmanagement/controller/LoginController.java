package com.schoolmanagement.schoolmanagement.controller;


import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import com.schoolmanagement.schoolmanagement.model.JwtResponse;
import com.schoolmanagement.schoolmanagement.model.LoginRequest;
import com.schoolmanagement.schoolmanagement.securityConfig.jwt.JwtUtils;
import com.schoolmanagement.schoolmanagement.securityConfig.services.UserDetailsImpl;
import com.schoolmanagement.schoolmanagement.service.LoginService;
import com.schoolmanagement.schoolmanagement.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

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

    @Autowired
    OtpService otpService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
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

        return ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam("email") String email) throws Exception {
        return ok(new ApiResponse<>(loginService.sendPasswordResetOtpViaMail(email)));
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<ApiResponse> validateOtp(@RequestParam("token") String token, @RequestParam("otp") String otp) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(otpService.validateOtp(token, otp)));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam("token") String token, @RequestParam("password") String password) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(loginService.resetPassword(token, bCryptPasswordEncoder.encode(password))));
    }
}
