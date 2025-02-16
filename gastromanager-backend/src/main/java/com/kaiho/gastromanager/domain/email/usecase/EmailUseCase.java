package com.kaiho.gastromanager.domain.email.usecase;

import com.kaiho.gastromanager.domain.email.api.EmailServicePort;
import com.kaiho.gastromanager.domain.email.model.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailUseCase implements EmailServicePort {

    private final JavaMailSender sender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    @Override
    public void sendEmail(String name, String to, EmailTemplateName emailTemplate, String confirmationUrl, String token, String subject) throws MessagingException {

        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.getName();
        }
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART_MODE_MIXED, UTF_8.name());

        Map<String, Object> props = new HashMap<>();

        props.put("name", name);
        props.put("confirmationUrl", confirmationUrl);
        props.put("token", token);

        Context context = new Context();
        context.setVariables(props);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        sender.send(message);

    }

}

