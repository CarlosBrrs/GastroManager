package com.kaiho.gastromanager.infrastructure.config.security;

import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.infrastructure.config.security.customfilters.JwtAuthFilter;
import com.kaiho.gastromanager.infrastructure.config.security.customfilters.RestaurantAuthFilter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.BASE_URL;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.CHEF;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.CONFIGS_URL;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.INGREDIENTS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.INGREDIENT_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.INVENTORY_MOVEMENT_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.KITCHEN_STAFF;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.MANAGER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.ORDERS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.ORDER_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.OWNER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.PRODUCT_ITEMS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.PRODUCT_ITEM_UUID_PARAMETER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.RESTAURANTS_CONTROLLER;
import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.RESTAURANT_UUID_PARAMETER;
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
            "/auth/login",
            "/auth/sign-up",
            "/auth/verify-account",
            "/subscription-plans/**",
            "/swagger-ui/**",
            "/v3/api-docs*/**"
    };
    private final JwtAuthFilter jwtAuthFilter;
    private final UserServicePort userServicePort;
    private final CustomBearerTokenAuthenticationEntryPoint authenticationEntryPoint;
    private final RestaurantAuthFilter restaurantAuthFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()

                        // ingredients controller
                        .requestMatchers(POST, INGREDIENTS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(GET, INGREDIENTS_CONTROLLER).authenticated() // ALL ROLES can access
                        .requestMatchers(GET, INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).authenticated() // ALL ROLES can access
                        .requestMatchers(PUT, INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(PATCH, INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER + "/deactivate").hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(PATCH, INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER + "/activate").hasAnyRole(SUPERUSER, OWNER, MANAGER, CHEF)
                        .requestMatchers(DELETE, INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(PATCH, INGREDIENTS_CONTROLLER + INGREDIENT_UUID_PARAMETER + "/adjust-ingredient-stock").hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // restaurant configs
                        .requestMatchers(POST, RESTAURANTS_CONTROLLER + CONFIGS_URL).hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(GET, RESTAURANTS_CONTROLLER + CONFIGS_URL).authenticated()

                        // users controller
                        .requestMatchers(POST, USERS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(GET, USERS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(GET, USERS_CONTROLLER + USER_UUID_PARAMETER).authenticated()
                        .requestMatchers(PUT, USERS_CONTROLLER + USER_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(PATCH, USERS_CONTROLLER + USER_UUID_PARAMETER + "/deactivate").hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(PATCH, USERS_CONTROLLER + USER_UUID_PARAMETER + "/activate").hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(PUT, USERS_CONTROLLER + USER_UUID_PARAMETER + ROLES_CONTROLLER).hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(DELETE, USERS_CONTROLLER + USER_UUID_PARAMETER + ROLES_CONTROLLER + ROLE_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER)

                        // roles controller
                        .requestMatchers(GET, ROLES_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(GET, ROLES_CONTROLLER + ROLE_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // product items controller
                        .requestMatchers(GET, PRODUCT_ITEMS_CONTROLLER).authenticated()
                        .requestMatchers(GET, PRODUCT_ITEMS_CONTROLLER + PRODUCT_ITEM_UUID_PARAMETER).authenticated()
                        .requestMatchers(POST, PRODUCT_ITEMS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        .requestMatchers(PUT, PRODUCT_ITEMS_CONTROLLER + PRODUCT_ITEM_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // orders controller
                        .requestMatchers(POST, ORDERS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER, WAITER)
                        .requestMatchers(GET, ORDERS_CONTROLLER).authenticated()
                        .requestMatchers(GET, ORDERS_CONTROLLER + ORDER_UUID_PARAMETER).authenticated()
                        .requestMatchers(PUT, ORDERS_CONTROLLER + ORDER_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER, WAITER)
                        .requestMatchers(PATCH, ORDERS_CONTROLLER + ORDER_UUID_PARAMETER + "/status").hasAnyRole(SUPERUSER, OWNER, MANAGER, WAITER, CHEF, KITCHEN_STAFF)
                        .requestMatchers(DELETE, ORDERS_CONTROLLER + ORDER_UUID_PARAMETER).hasAnyRole(SUPERUSER, OWNER, MANAGER)

                        // restaurants controller
                        .requestMatchers(GET, BASE_URL + RESTAURANTS_CONTROLLER).hasAnyRole(SUPERUSER)
                        .requestMatchers(GET, BASE_URL + RESTAURANTS_CONTROLLER + RESTAURANT_UUID_PARAMETER).hasAnyRole(SUPERUSER)
                        .requestMatchers(PUT, BASE_URL + RESTAURANTS_CONTROLLER + RESTAURANT_UUID_PARAMETER).hasAnyRole(SUPERUSER)
                        .requestMatchers(POST, RESTAURANTS_CONTROLLER).hasAnyRole(SUPERUSER, OWNER)
                        .requestMatchers(PATCH, BASE_URL + RESTAURANTS_CONTROLLER + RESTAURANT_UUID_PARAMETER).hasAnyRole(SUPERUSER)
                        .requestMatchers(DELETE, BASE_URL + RESTAURANTS_CONTROLLER + RESTAURANT_UUID_PARAMETER).hasAnyRole(SUPERUSER)


                        .requestMatchers(GET, BASE_URL + RESTAURANTS_CONTROLLER + "/**").hasAnyRole(SUPERUSER)


                        // inventory movements controller
                        .requestMatchers(POST, BASE_URL + INVENTORY_MOVEMENT_CONTROLLER).hasAnyRole(SUPERUSER, OWNER, MANAGER)
                        // superuser matcher has to be below all the rest controller matchers and before deny all
//                        .requestMatchers("/**").hasRole(SUPERUSER) // superuser has full authorities- Not working yet
                        .anyRequest().denyAll()) // All other requests must be denied
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
                .exceptionHandling(ex -> ex.authenticationEntryPoint(this.authenticationEntryPoint))
                .addFilterAfter(restaurantAuthFilter, UsernamePasswordAuthenticationFilter.class)
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedOrigin("https://gastromanager-frontend.onrender.com");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
