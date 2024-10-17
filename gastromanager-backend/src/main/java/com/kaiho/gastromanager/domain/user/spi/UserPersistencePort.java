package com.kaiho.gastromanager.domain.user.spi;

import com.kaiho.gastromanager.domain.user.model.User;

import java.util.Optional;

public interface UserPersistencePort {
    User createUser(User user);

    Optional<User> findUserByUsername(String username);
}
