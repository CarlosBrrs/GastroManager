package com.kaiho.gastromanager.domain.user.spi;

import com.kaiho.gastromanager.domain.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    User createUser(User user);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUuid(UUID uuid);
}
