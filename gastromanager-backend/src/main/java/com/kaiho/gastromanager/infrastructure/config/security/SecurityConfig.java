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
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.CHEF;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.INGREDIENTS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.INGREDIENT_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.KITCHEN_STAFF;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.MANAGER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.ORDERS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.ORDER_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.OWNER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.PRODUCT_ITEMS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.PRODUCT_ITEM_UUID_PARAMETER;
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
                        .requestMatchers(POST, BASE_URL + INGREDIENTS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(GET, BASE_URL + INGREDIENTS_CONTROLLER).authenticated() // ALL ROLES can access
                        .requestMatchers(GET, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).authenticated() // ALL ROLES can access
                        .requestMatchers(PUT, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(PATCH, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER + "/deactivate").hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(PATCH, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER + "/activate").hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(DELETE, BASE_URL + INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // users controller
                        .requestMatchers(POST, BASE_URL + USERS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(GET, BASE_URL + USERS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(GET, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(PUT, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(PATCH, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + "/deactivate").hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(PATCH, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + "/activate").hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(PUT, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + ROLES_CONTROLLER).hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(DELETE, BASE_URL + USERS_CONTROLLER + USER_UUID_PARAMETER + ROLES_CONTROLLER + ROLE_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER)

                        // roles controller
                        .requestMatchers(GET, BASE_URL + ROLES_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(GET, BASE_URL + ROLES_CONTROLLER + ROLE_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // product items controller
                        .requestMatchers(GET, BASE_URL + PRODUCT_ITEMS_CONTROLLER).authenticated()
                        .requestMatchers(GET, BASE_URL + PRODUCT_ITEMS_CONTROLLER + PRODUCT_ITEM_UUID_PARAMETER).authenticated()
                        .requestMatchers(POST, BASE_URL + PRODUCT_ITEMS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(PUT, BASE_URL + PRODUCT_ITEMS_CONTROLLER + PRODUCT_ITEM_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // orders controller
                        .requestMatchers(POST, BASE_URL + ORDERS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER, WAITER)
                        .requestMatchers(GET, BASE_URL + ORDERS_CONTROLLER).authenticated()
                        .requestMatchers(GET, BASE_URL + ORDERS_CONTROLLER + ORDER_UUID_PARAMETER).authenticated()
                        .requestMatchers(PUT, BASE_URL + ORDERS_CONTROLLER + ORDER_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER, WAITER)
                        .requestMatchers(PATCH, BASE_URL + ORDERS_CONTROLLER + ORDER_UUID_PARAMETER + "/status").hasAnyRole(SUPERUSER, OWNER, MANAGER, WAITER, CHEF, KITCHEN_STAFF)
                        .requestMatchers(DELETE, BASE_URL + ORDERS_CONTROLLER + ORDER_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // superuser matcher has to be below all the rest controller matchers and before deny all
//                        .requestMatchers("/**").hasRole(SUPERUSER) // superuser has full authorities- Not working yet
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
