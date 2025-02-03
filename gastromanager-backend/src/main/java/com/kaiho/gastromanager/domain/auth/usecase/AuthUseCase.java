package com.kaiho.gastromanager.domain.auth.usecase;

import com.kaiho.gastromanager.domain.auth.api.AuthServicePort;
import com.kaiho.gastromanager.domain.auth.api.VerificationTokenServicePort;
import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.domain.auth.model.Signup;
import com.kaiho.gastromanager.domain.auth.model.Subscription;
import com.kaiho.gastromanager.domain.auth.model.VerificationToken;
import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUseCase implements AuthServicePort {

    private final UserServicePort userServicePort;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserPersistencePort userPersistencePort;
    private final VerificationTokenServicePort verificationTokenServicePort;

    @Override
    @Transactional
    public Map<String, Object> login(Login login) {

        User user = (User) userServicePort.loadUserByUsername(login.username());

        if (!passwordEncoder.matches(login.password(), user.getEncodedPassword())) {
            throw new BadCredentialsException("Password incorrect.");
        }

        if (verificationTokenServicePort.isVerificationPending(user)) {
            throw new IllegalStateException("Verification pending. Please verify your account to login");
        }
        String jwtToken = jwtTokenProvider.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("jwtToken", jwtToken);

        if (user.getRestaurant() != null) {
            response.put("restaurantUuid", user.getRestaurant().getUuid());
        } else if (user.getRestaurants() != null && !user.getRestaurants().isEmpty()) {
            List<Map<String, String>> restaurants = user.getRestaurants().stream()
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

    @Override
    @Transactional
    public void signup(Signup signup) {
        User user = signup.getUser();
        if (userServicePort.isUserAlreadyRegistered(user)) {
            if (verificationTokenServicePort.isVerificationPending(user)) {
                throw new IllegalStateException("El usuario ya está pendiente de verificación.");
            } else {
                throw new IllegalStateException("El usuario ya existe y está verificado.");
            }
        }

        validateUserInformation(user);
        validateSubscriptionInformation(signup.getSubscription());

        User createdUser = userPersistencePort.createUser(user);

        verificationTokenServicePort.createVerificationToken(createdUser);
    }

    @Override
    @Transactional
    public UUID verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenServicePort.findByToken(token);
        if (verificationToken.isExpired()) {
            throw new IllegalStateException("El token ha expirado.");
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        User verifiedUser = userServicePort.updateUser(user);

        verificationTokenServicePort.deleteToken(verificationToken);

        return verifiedUser.getUuid();
    }

    private void validateSubscriptionInformation(Subscription subscription) {
        log.info("Validations for subscription");
    }

    private void validateUserInformation(User user) {
        log.info("Validations for user");
    }

}
