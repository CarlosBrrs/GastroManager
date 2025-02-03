package com.kaiho.gastromanager.application.auth.handler;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.SignupRequestDto;
import com.kaiho.gastromanager.application.auth.mapper.AuthMapper;
import com.kaiho.gastromanager.domain.auth.api.AuthServicePort;
import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.domain.auth.model.Signup;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

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

    @Override
    public ApiGenericResponse<UUID> signup(SignupRequestDto signupRequestDto) {
        Signup signup = authMapper.toDomain(signupRequestDto);
        authServicePort.signup(signup);
        return buildSuccessResponse("User registered, please check your email to verify your account. You have 5 mins", null);
    }

    @Override
    public ApiGenericResponse<UUID> verifyAccount(String token) {
        UUID verifiedUserUuid = authServicePort.verifyAccount(token);
        return buildSuccessResponse("User verified successfully, you can login now", verifiedUserUuid);
    }

}
