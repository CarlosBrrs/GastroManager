package com.kaiho.gastromanager.domain.auth.usecase;

import com.kaiho.gastromanager.domain.auth.api.AuthServicePort;
import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthServicePort {

    private final UserServicePort userServicePort;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String login(Login login) {

        User user = (User) userServicePort.loadUserByUsername(login.username());

        if (!passwordEncoder.matches(login.password(), user.encodedPassword())) {
            throw new BadCredentialsException("Password incorrect.");
        }
        return jwtTokenProvider.generateToken(user);
    }

}
