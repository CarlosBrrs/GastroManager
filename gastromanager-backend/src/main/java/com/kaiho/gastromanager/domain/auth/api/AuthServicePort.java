package com.kaiho.gastromanager.domain.auth.api;

import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.domain.auth.model.Signup;
import jakarta.mail.MessagingException;

import java.util.Map;
import java.util.UUID;

public interface AuthServicePort {
    Map<String, Object> login(Login login);

    UUID signup(Signup signup) throws MessagingException;

    UUID verifyAccount(String token) throws MessagingException;
}
