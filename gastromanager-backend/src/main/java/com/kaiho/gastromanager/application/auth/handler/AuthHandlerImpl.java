package com.kaiho.gastromanager.application.auth.handler;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.application.auth.mapper.AuthMapper;
import com.kaiho.gastromanager.domain.auth.api.AuthServicePort;
import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class AuthHandlerImpl implements AuthHandler {

    private final AuthMapper authMapper;
    private final AuthServicePort authServicePort;

    @Override
    public ApiGenericResponse<String> loginUser(LoginRequestDto loginRequestDto) {
        Login login = authMapper.toDomain(loginRequestDto);
        String jwt = authServicePort.login(login);
        return buildSuccessResponse("Login successful", jwt);
    }

}
