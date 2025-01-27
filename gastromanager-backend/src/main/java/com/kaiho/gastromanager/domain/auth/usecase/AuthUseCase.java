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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthServicePort {

    private final UserServicePort userServicePort;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Map<String, Object> login(Login login) {

        User user = (User) userServicePort.loadUserByUsername(login.username());

        if (!passwordEncoder.matches(login.password(), user.encodedPassword())) {
            throw new BadCredentialsException("Password incorrect.");
        }
        // Genera el token JWT
        String jwtToken = jwtTokenProvider.generateToken(user);

        // Prepara la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("jwtToken", jwtToken);

        if (user.restaurant() != null) {
            response.put("restaurantUuid", user.restaurant().getUuid());
        } else if (user.restaurants() != null && !user.restaurants().isEmpty()) {
            List<Map<String, String>> restaurants = user.restaurants().stream()
                    .map(rest -> {
                        Map<String, String> restaurantMap = new HashMap<>();
                        restaurantMap.put("uuid", rest.getUuid().toString());
                        restaurantMap.put("name", rest.getName());
                        return restaurantMap;
                    })
                    .toList();
            response.put("restaurants", restaurants);
        } else {
            throw new IllegalStateException("User has no restaurant information.");
        }
        return response;
    }

}
