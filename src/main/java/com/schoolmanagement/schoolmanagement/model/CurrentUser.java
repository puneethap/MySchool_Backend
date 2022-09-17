package com.schoolmanagement.schoolmanagement.model;

import com.schoolmanagement.schoolmanagement.entity.Erole;
import lombok.Data;

import java.util.List;
@Data
public class CurrentUser {
    private String userName;
    private Long userId;

    private String email;
    private List<Erole> role;

    public CurrentUser() {
    }

    public CurrentUser(String userName, Long userId, String email, List<Erole> role) {
        this.userName = userName;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}
