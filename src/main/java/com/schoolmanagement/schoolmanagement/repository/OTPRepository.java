package com.schoolmanagement.schoolmanagement.repository;

import com.schoolmanagement.schoolmanagement.entity.UserOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<UserOTP, Long> {
}
