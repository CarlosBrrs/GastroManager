package com.kaiho.gastromanager.application.auth.handler;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.application.auth.mapper.AuthMapper;
import com.kaiho.gastromanager.domain.auth.api.AuthServicePort;
import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class AuthHandlerImpl implements AuthHandler {

    private final AuthMapper authMapper;
    private final AuthServicePort authServicePort;

    @Override
    public ApiGenericResponse<Map<String, Object>> loginUser(LoginRequestDto loginRequestDto) {
        Login login = authMapper.toDomain(loginRequestDto);
        Map<String, Object> response = authServicePort.login(login);
        return buildSuccessResponse("Login successful", response);
    }

}
