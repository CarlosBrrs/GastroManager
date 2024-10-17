package com.kaiho.gastromanager.application.user.handler;

import com.kaiho.gastromanager.application.user.dto.request.UserRequestDto;
import com.kaiho.gastromanager.application.user.dto.response.UserResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;

public interface UserHandler {
    ApiGenericResponse<UserResponseDto> createUser(UserRequestDto userRequestDto);
}
