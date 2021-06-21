package com.bavostepbros.leap.domain.service.emailservice;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

// import freemarker.template.TemplateException;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendNewUserMessage(String to, String password);

    void sendForgotPassword(String to, String password);
}
