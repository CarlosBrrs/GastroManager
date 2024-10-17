package com.kaiho.gastromanager.domain.user.usecase;

import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUseCase implements UserServicePort {

    private final UserPersistencePort userPersistencePort;

    @Override
    @Transactional
    public User createUser(User user) {
        return userPersistencePort.createUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userPersistencePort.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " does not exist."));
        return User.builder()
                .uuid(user.uuid())
                .name(user.name())
                .lastname(user.lastname())
                .phone(user.phone())
                .email(user.email())
                .encodedPassword(user.encodedPassword())
                .username(user.username())
                .roles(user.roles())
                .build();
    }
}
