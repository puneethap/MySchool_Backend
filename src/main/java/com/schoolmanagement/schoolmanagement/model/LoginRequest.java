package com.schoolmanagement.schoolmanagement.model;

import com.schoolmanagement.schoolmanagement.constant.Messages;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

    @NotNull(message = Messages.USERNAME_IS_NULL)
    @NotBlank(message = Messages.USERNAME_IS_BLANK)
    private String username;

    @NotNull(message = Messages.PASSWORD_IS_NULL)
    @NotBlank(message = Messages.PASSWORD_IS_BLANK)
    private String password;

}
