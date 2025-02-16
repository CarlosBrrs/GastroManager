package com.kaiho.gastromanager.domain.email.api;

import com.kaiho.gastromanager.domain.email.model.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailServicePort {

    void sendEmail(String name, String to, EmailTemplateName emailTemplate, String confirmationUrl, String token, String subject) throws MessagingException;
}
