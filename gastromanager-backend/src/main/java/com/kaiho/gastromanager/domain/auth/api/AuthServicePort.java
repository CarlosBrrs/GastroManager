package com.kaiho.gastromanager.domain.auth.api;

import com.kaiho.gastromanager.domain.auth.model.Login;

public interface AuthServicePort {
    String login(Login login);

}
