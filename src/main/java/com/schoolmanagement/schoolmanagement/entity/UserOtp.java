package com.schoolmanagement.schoolmanagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "user_otp", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id"), @UniqueConstraint(columnNames = "email")})
@Data
public class UserOtp {

    public UserOtp() {
    }

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String email;
    private String token;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;

    private String otp;

    private String otpValidated;

    public UserOtp(Long userId, String email, String token, LocalDateTime tokenCreationDate, String otp) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.tokenCreationDate = tokenCreationDate;
        this.otp = otp;
    }
}
