package com.kaiho.gastromanager.infrastructure.auth.input.rest;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.application.auth.handler.AuthHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthHandler authHandler;

    @PostMapping("/login")
    public ResponseEntity<ApiGenericResponse<String>> login(@RequestBody LoginRequestDto loginRequestDto) {
        ApiGenericResponse<String> loggedUser = authHandler.loginUser(loginRequestDto);
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }

}
