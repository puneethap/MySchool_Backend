package com.schoolmanagement.schoolmanagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "user_otp", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id"), @UniqueConstraint(columnNames = "email")})
@Data
public class UserOTP {

    public UserOTP() {
    }

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String email;
    private String token;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;

    private String OTP;

    public UserOTP(Long userId, String email, String token, LocalDateTime tokenCreationDate, String OTP) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.tokenCreationDate = tokenCreationDate;
        this.OTP = OTP;
    }
}
