package com.schoolmanagement.schoolmanagement.controller;


import com.schoolmanagement.schoolmanagement.customAnnotations.interfaces.ValidatedOtp;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mySchool")
@Validated
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
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
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
    public ResponseEntity<ApiResponse> forgotPassword(@NotNull(message = "Email is null")
                                                      @NotBlank(message = "Email is blank")
                                                      @Email(message = "Invalid Email")
                                                      @RequestParam("email")
                                                      String email) throws Exception {
        return ok(new ApiResponse<>(otpService.sendPasswordResetOtpViaMail(email)));
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<ApiResponse> validateOtp(@NotNull(message = "Token is null")
                                                   @NotBlank(message = "Token is blank")
                                                   @RequestParam("token")
                                                   String token,

                                                   @NotNull(message = "OTP is null")
                                                   @NotBlank(message = "OTP is blank")
                                                   @Size(min = 4, max = 4, message = "OTP should be 4 digits")
                                                   @RequestParam("otp")
                                                   String otp) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(otpService.validateOtp(token, otp)));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@NotNull(message = "Token is null")
                                                     @NotBlank(message = "Token is blank")
                                                     @ValidatedOtp
                                                     @RequestParam("token")
                                                     String token,

                                                     @NotNull(message = "Password is null")
                                                     @NotBlank(message = "Password is blank")
                                                     @RequestParam("password")
                                                     String password) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(loginService.resetPassword(token, bCryptPasswordEncoder.encode(password))));
    }
}
