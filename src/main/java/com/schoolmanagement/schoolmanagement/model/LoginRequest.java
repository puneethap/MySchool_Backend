package com.schoolmanagement.schoolmanagement.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @NotNull(message = "Username is null")
    @NotBlank(message = "Username is blank")
    private String username;

    @NotNull(message = "password is null")
    @NotBlank(message = "Password is blank")
    private String password;

}
