package com.kaiho.gastromanager.application.auth.handler;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

import java.util.Map;

public interface AuthHandler {

    ApiGenericResponse<Map<String, Object>> loginUser(LoginRequestDto loginRequestDto);

}
