package com.kaiho.gastromanager.infrastructure.config.security;

import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.BASE_URL;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.CASHIER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.CHEF;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.INGREDIENTS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.INGREDIENT_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.KITCHEN_STAFF;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.MANAGER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.OWNER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.ROLES_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.ROLE_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.SUPERUSER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.USERS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.USER_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.WAITER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] WHITE_LIST_URL = {
            BASE_URL + "/auth/login"
    };
    private final JwtAuthFilter jwtAuthFilter;
    private final UserServicePort userServicePort;
    private final CustomBearerTokenAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disabling CSRF as we use JWT which is immune to CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll() // Whitelisting some paths from authentication

                        // ingredients controller
                        .requestMatchers(POST, BASE_URL + INGREDIENTS_CONTROLLER).hasAnyRole(OWNER, MANAGER, CHEF)
                        .requestMatchers(GET, BASE_URL + INGREDIENTS_CONTROLLER).hasAnyRole(OWNER, MANAGER, CHEF, KITCHEN_STAFF, CASHIER, WAITER) // ALL ROLES can access
                        .requestMatchers(GET, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).hasAnyRole(OWNER, MANAGER, CHEF, KITCHEN_STAFF, CASHIER, WAITER) // ALL ROLES can access
                        .requestMatchers(PUT, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).hasAnyRole(OWNER, MANAGER, CHEF)
                        .requestMatchers(PATCH, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER + "/deactivate").hasAnyRole(OWNER, MANAGER, CHEF)
                        .requestMatchers(PATCH, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER + "/activate").hasAnyRole(OWNER, MANAGER, CHEF)
                        .requestMatchers(DELETE, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).hasAnyRole(OWNER, MANAGER)

                        // users controller
                        .requestMatchers(POST, BASE_URL + USERS_CONTROLLER).hasAnyRole(OWNER, MANAGER)
                        .requestMatchers(GET, BASE_URL + USERS_CONTROLLER).hasAnyRole(OWNER, MANAGER)
                        .requestMatchers(GET, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER).hasAnyRole(OWNER, MANAGER)
                        .requestMatchers(PUT, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER).hasAnyRole(OWNER, MANAGER)
                        .requestMatchers(PATCH, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + "/deactivate").hasRole(OWNER)
                        .requestMatchers(PATCH, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + "/activate").hasRole(OWNER)
                        .requestMatchers(PUT, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + ROLES_CONTROLLER).hasRole(OWNER)
                        .requestMatchers(DELETE, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + ROLES_CONTROLLER + ROLE_UUID_PARAMETER).hasRole(OWNER)

                        // roles controller
                        .requestMatchers(GET, BASE_URL + ROLES_CONTROLLER).hasAnyRole(OWNER, MANAGER)
                        .requestMatchers(GET, BASE_URL + ROLES_CONTROLLER + ROLE_UUID_PARAMETER).hasAnyRole(OWNER, MANAGER)

                        .requestMatchers("/**").hasRole(SUPERUSER) // superuser has full authorities
                        .anyRequest().denyAll()) // All other requests must be denied
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
                .exceptionHandling(ex -> ex.authenticationEntryPoint(this.authenticationEntryPoint))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Registering our JwtAuthFilter
                .build();
    }

    @Bean
    AuthenticationManager authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServicePort); // Setting our custom user details service
        provider.setPasswordEncoder(passwordEncoder()); // Setting the password encoder
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
