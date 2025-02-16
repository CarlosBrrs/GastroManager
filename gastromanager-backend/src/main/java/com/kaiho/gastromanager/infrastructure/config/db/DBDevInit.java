package com.kaiho.gastromanager.infrastructure.config.db;

import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.RoleEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.entity.UserEntity;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.RoleEntityRepository;
import com.kaiho.gastromanager.infrastructure.user.output.jpa.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DBDevInit implements CommandLineRunner {
    private final RoleEntityRepository roleEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists();

    }

    private void createUserIfNotExists() {
        Optional<UserEntity> cbarrios = userEntityRepository.findByUsername("cbarrios");
        RoleEntity ownerRole = roleEntityRepository.findById(UUID.fromString("754234e5-8c3d-4d13-b5de-3b4de4a4bc99")).orElseThrow();
        if (cbarrios.isPresent()) {
            cbarrios.get().setEncodedPassword(passwordEncoder.encode("cbarrios"));
            cbarrios.get().setRoles(Set.of(ownerRole));
            userEntityRepository.save(cbarrios.get());
            log.error("User created successfully");
        } else {
            log.error("Critical error: The user cbarrios does not exists. Please contact admin");
        }
    }

}
