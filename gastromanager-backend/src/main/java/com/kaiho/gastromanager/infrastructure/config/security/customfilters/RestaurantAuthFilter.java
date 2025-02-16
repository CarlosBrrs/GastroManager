package com.kaiho.gastromanager.infrastructure.config.security.customfilters;

import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.common.exceptionhandler.MissingRestaurantUuidHeaderException;
import com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.constant.Constants.BASE_URL;

@Component
@RequiredArgsConstructor
public class RestaurantAuthFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDED_PATHS = List.of("/api/v1/subscription-plans", "/api/v1/users", "/api/v1/auth/login", "/api/v1/auth/sign-up", BASE_URL + "/auth/verify-account");
    private static final String RESTAURANT_UUID_HEADER = "X-Restaurant-Uuid";
    private final RestaurantServicePort restaurantServicePort;
    private final UserServicePort userServicePort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (isExcludedPath(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String restaurantUuid = request.getHeader(RESTAURANT_UUID_HEADER);
        if (restaurantUuid == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing required header: X-Restaurant-Uuid");
            throw new MissingRestaurantUuidHeaderException();
        }
        try {
            validateAccessToRestaurant(restaurantUuid);

            RestaurantContext.setCurrentRestaurant(UUID.fromString(restaurantUuid));

            filterChain.doFilter(request, response);

        } finally {
            RestaurantContext.clear();
        }
    }

    private void validateAccessToRestaurant(String restaurantUuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated.");
        }

        User principal = (User) authentication.getPrincipal();
        Restaurant restaurant = restaurantServicePort.getRestaurantById(UUID.fromString(restaurantUuid));

        if (!userServicePort.hasAccessToRestaurant(principal, restaurant)) {
            throw new AccessDeniedException("User " + principal.getUuid() + " has no access to this restaurant");
        }

    }

    private boolean isExcludedPath(String requestUri) {
        return EXCLUDED_PATHS.stream().anyMatch(requestUri::startsWith);
    }

}