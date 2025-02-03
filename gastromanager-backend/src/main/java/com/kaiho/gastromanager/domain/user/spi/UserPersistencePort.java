package com.kaiho.gastromanager.domain.user.spi;

import com.kaiho.gastromanager.domain.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    User createUser(User user);

    Optional<User> findUserByUsername(String username);

    Optional<User> getUserByUuid(UUID uuid);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    boolean phoneExists(String phone);

    boolean isUserAlreadyRegistered(User user);

    User updateUser(User updatedUser);
}
