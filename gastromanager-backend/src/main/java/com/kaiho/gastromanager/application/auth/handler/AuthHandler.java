package com.kaiho.gastromanager.application.auth.handler;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.SignupRequestDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.Map;
import java.util.UUID;

public interface AuthHandler {

    ApiGenericResponse<Map<String, Object>> loginUser(LoginRequestDto loginRequestDto);

    ApiGenericResponse<UUID> signup(SignupRequestDto signupRequestDto);

    ApiGenericResponse<UUID> verifyAccount(String token);
}
