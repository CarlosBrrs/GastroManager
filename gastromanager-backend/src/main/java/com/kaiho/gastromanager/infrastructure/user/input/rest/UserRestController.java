package com.kaiho.gastromanager.infrastructure.user.input.rest;

import com.kaiho.gastromanager.application.user.dto.request.UserRequestDto;
import com.kaiho.gastromanager.application.user.dto.response.UserResponseDto;
import com.kaiho.gastromanager.application.user.handler.UserHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserHandler userHandler;

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UserResponseDto>> createUser(@RequestBody UserRequestDto userRequestDto) {
        ApiGenericResponse<UserResponseDto> user = userHandler.createUser(userRequestDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}

