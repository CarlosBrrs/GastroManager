package com.kaiho.gastromanager.domain.auth.api;

import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.domain.auth.model.Signup;

import java.util.Map;
import java.util.UUID;

public interface AuthServicePort {
    Map<String, Object> login(Login login);

    void signup(Signup signup);

    UUID verifyAccount(String token);
}
