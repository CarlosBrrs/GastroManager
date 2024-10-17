package com.kaiho.gastromanager.application.auth.mapper;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.domain.auth.model.Login;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public Login toDomain(LoginRequestDto loginRequestDto) {
        if (loginRequestDto == null) {
            return null;
        }
        return Login.builder()
                .username(loginRequestDto.username())
                .password(loginRequestDto.password())
                .build();
    }

}
