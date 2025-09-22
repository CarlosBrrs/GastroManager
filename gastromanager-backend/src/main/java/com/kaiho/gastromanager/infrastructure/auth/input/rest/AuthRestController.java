package com.kaiho.gastromanager.infrastructure.auth.input.rest;

import com.kaiho.gastromanager.application.auth.dto.request.LoginRequestDto;
import com.kaiho.gastromanager.application.auth.dto.request.SignupRequestDto;
import com.kaiho.gastromanager.application.auth.handler.AuthHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthHandler authHandler;

    @PostMapping("/login")
    public ResponseEntity<ApiGenericResponse<Map<String, Object>>> login(@RequestBody LoginRequestDto loginRequestDto) {
        ApiGenericResponse<Map<String, Object>> response = authHandler.loginUser(loginRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiGenericResponse<UUID>> signup(@RequestBody SignupRequestDto signupRequestDto) throws MessagingException {
        ApiGenericResponse<UUID> response = authHandler.signup(signupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<ApiGenericResponse<UUID>> verifyAccount(@RequestParam String token) throws MessagingException {
        ApiGenericResponse<UUID> response = authHandler.verifyAccount(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
