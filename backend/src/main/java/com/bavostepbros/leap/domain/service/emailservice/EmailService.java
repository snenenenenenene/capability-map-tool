package com.bavostepbros.leap.domain.service.emailservice;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendNewUserMessage(String to, String password);

    void sendForgotPassword(String to, String password);
}
