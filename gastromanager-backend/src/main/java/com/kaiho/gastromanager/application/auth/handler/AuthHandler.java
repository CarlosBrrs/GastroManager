package com.kaiho.gastromanager.application.auth.handler;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

public interface AuthHandler {

    ApiGenericResponse<String> loginUser(LoginRequestDto loginRequestDto);

}
