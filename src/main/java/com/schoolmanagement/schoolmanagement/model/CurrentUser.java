package com.schoolmanagement.schoolmanagement.model;

import com.schoolmanagement.schoolmanagement.entity.Erole;

import java.util.List;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Erole> getRole() {
        return role;
    }

    public void setRole(List<Erole> role) {
        this.role = role;
    }
}
