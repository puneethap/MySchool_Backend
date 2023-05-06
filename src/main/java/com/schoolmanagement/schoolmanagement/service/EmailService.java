package com.schoolmanagement.schoolmanagement.service;

public interface EmailService {
    void sendSimpleMail(String toEmail, String subject, String body) throws Exception;
}
