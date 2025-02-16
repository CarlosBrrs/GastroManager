package com.kaiho.gastromanager.domain.user.api;

import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import com.kaiho.gastromanager.domain.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserServicePort extends UserDetailsService {

    User getUserByUuid(UUID userUuid);

    User createUser(User user);

    boolean hasAccessToRestaurant(User user, Restaurant restaurant);

    boolean isUserAlreadyRegistered(User user);

    User updateUser(User user);

    Optional<User> getUserByUuidOrEmail(String username, String email);
}
