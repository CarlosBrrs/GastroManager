package com.kaiho.gastromanager.domain.auth.api;

import com.kaiho.gastromanager.domain.auth.model.Login;

import java.util.Map;

public interface AuthServicePort {
    Map<String, Object> login(Login login);

}
