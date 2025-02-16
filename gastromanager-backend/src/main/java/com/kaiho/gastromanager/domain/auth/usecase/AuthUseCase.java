package com.kaiho.gastromanager.domain.auth.usecase;

import com.kaiho.gastromanager.domain.auth.api.AuthServicePort;
import com.kaiho.gastromanager.domain.auth.api.VerificationTokenServicePort;
import com.kaiho.gastromanager.domain.auth.exception.ExpiredTokenException;
import com.kaiho.gastromanager.domain.auth.model.Login;
import com.kaiho.gastromanager.domain.auth.model.Signup;
import com.kaiho.gastromanager.domain.auth.model.Subscription;
import com.kaiho.gastromanager.domain.auth.model.VerificationToken;
import com.kaiho.gastromanager.domain.email.api.EmailServicePort;
import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import com.kaiho.gastromanager.infrastructure.config.security.JwtTokenProvider;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.kaiho.gastromanager.domain.email.model.EmailTemplateName.VERIFY_ACCOUNT;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUseCase implements AuthServicePort {

    private final UserServicePort userServicePort;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserPersistencePort userPersistencePort;
    private final VerificationTokenServicePort verificationTokenServicePort;
    private final EmailServicePort emailServicePort;
    @Value("${spring.application.mailing.frontend.confirmation-url}")
    private String confirmationUrl;

    @Override
    @Transactional
    public Map<String, Object> login(Login login) {

        User user = (User) userServicePort.loadUserByUsername(login.username());

        if (!passwordEncoder.matches(login.password(), user.getEncodedPassword())) {
            throw new BadCredentialsException("Password incorrect.");
        }

        if (!user.isVerified()) {
            throw new IllegalStateException("Verification pending. Please verify your account to login");
        }
        String jwtToken = jwtTokenProvider.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("jwtToken", jwtToken);

        List<Map<String, String>> restaurants = new ArrayList<>();

        if (user.getRestaurant() != null) {
            Map<String, String> restaurantMap = new HashMap<>();
            restaurantMap.put("uuid", user.getRestaurant().getUuid().toString());
            restaurantMap.put("name", user.getRestaurant().getName());
            restaurants.add(restaurantMap);
        } else if (user.getRestaurants() != null && !user.getRestaurants().isEmpty()) {
            restaurants = user.getRestaurants().stream()
                    .map(rest -> {
                        Map<String, String> restaurantMap = new HashMap<>();
                        restaurantMap.put("uuid", rest.getUuid().toString());
                        restaurantMap.put("name", rest.getName());
                        return restaurantMap;
                    })
                    .toList();
        }

        response.put("restaurants", restaurants);
        return response;
    }

    @Override
    @Transactional
    public UUID signup(Signup signup) throws MessagingException {
        User user = signup.getUser();
        Optional<User> userByUuidOrEmail = userServicePort.getUserByUuidOrEmail(user.getUsername(), user.getContact().getEmail());
        if (userByUuidOrEmail.isPresent()) {
            if (!userByUuidOrEmail.get().isVerified()) {
                throw new IllegalStateException("El usuario ya está pendiente de verificación.");
            } else {
                throw new IllegalStateException("El usuario ya existe y está verificado. Dirijase al login para acceder a la plataforma");
            }
        }

        validateUserInformation(user);
        validateSubscriptionInformation(signup.getSubscription());

        User createdUser = userPersistencePort.createUser(user);

        VerificationToken verificationToken = verificationTokenServicePort.createVerificationToken(createdUser);

        sendVerificationEmail(verificationToken);

        return createdUser.getUuid();
    }


    private void sendVerificationEmail(VerificationToken verificationToken) throws MessagingException {
        emailServicePort.sendEmail(verificationToken.getUser().getName(), verificationToken.getUser().getContact().getEmail(), VERIFY_ACCOUNT, confirmationUrl, String.valueOf(verificationToken.getToken()), "ACCOUNT ACTIVATION");
    }

    @Override
    @Transactional(noRollbackFor = ExpiredTokenException.class)
    public UUID verifyAccount(String token) throws MessagingException {
        //TODO: CAPTURAR LA EXCEPCION
        VerificationToken verificationToken = verificationTokenServicePort.findByToken(token);
        if (verificationToken.isExpired()) {
            verificationTokenServicePort.deleteToken(verificationToken);
            VerificationToken newToken = verificationTokenServicePort.createVerificationToken(verificationToken.getUser());

            sendVerificationEmail(newToken);
            throw new ExpiredTokenException("El token ha expirado. Se envio un nuevo token al correo.");
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
