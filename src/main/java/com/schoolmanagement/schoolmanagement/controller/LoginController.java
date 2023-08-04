package com.schoolmanagement.schoolmanagement.controller;


import com.schoolmanagement.schoolmanagement.config.securityConfig.jwt.JwtUtils;
import com.schoolmanagement.schoolmanagement.config.securityConfig.services.UserDetailsImpl;
import com.schoolmanagement.schoolmanagement.constant.Messages;
import com.schoolmanagement.schoolmanagement.customAnnotations.interfaces.ValidatedOtp;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import com.schoolmanagement.schoolmanagement.model.JwtResponse;
import com.schoolmanagement.schoolmanagement.model.LoginRequest;
import com.schoolmanagement.schoolmanagement.service.LoginService;
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
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    LoginService loginService;

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
    public ResponseEntity<ApiResponse> forgotPassword(@NotBlank(message = Messages.EMAIL_IS_BLANK)
                                                      @Email(message = Messages.INVALID_EMAIL)
                                                      @RequestParam("email")
                                                      String email) throws Exception {
        return ok(new ApiResponse<>(loginService.sendPasswordResetOtp(email)));
    }

    @PostMapping("/validatePasswordResetOtp")
    public ResponseEntity<ApiResponse> validatePasswordResetOtp(@NotBlank(message = Messages.TOKEN_IS_BLANK)
                                                                @RequestParam("token")
                                                                String token,

                                                                @NotBlank(message = Messages.OTP_IS_BLANK)
                                                                @Size(min = 4, max = 4, message = Messages.OTP_LENGTH_ERROR + " {max}")
                                                                @RequestParam("otp")
                                                                String otp) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(loginService.validatePasswordResetOtp(token, otp)));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@NotBlank(message = Messages.TOKEN_IS_BLANK)
                                                     @ValidatedOtp
                                                     @RequestParam("token")
                                                     String token,

                                                     @NotBlank(message = Messages.PASSWORD_IS_BLANK)
                                                     @RequestParam("password")
                                                     String password) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(loginService.resetPassword(token, bCryptPasswordEncoder.encode(password))));
    }
}
