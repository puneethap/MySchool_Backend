package com.schoolmanagement.schoolmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;

public interface EmailService {
    public void sendSimpleMail(String toEmail, String subject, String body) throws Exception;
}
