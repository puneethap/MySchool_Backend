package com.schoolmanagement.schoolmanagement.service;

public interface EmailService {
    public void sendSimpleMail(String toEmail, String subject, String body) throws Exception;
}
