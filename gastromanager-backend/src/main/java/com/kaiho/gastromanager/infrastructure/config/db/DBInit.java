package com.kaiho.gastromanager.infrastructure.config.db;

import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.RoleType;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.domain.user.spi.RolePersistencePort;
import com.kaiho.gastromanager.domain.user.spi.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DBInit implements CommandLineRunner {

    private final UserPersistencePort userPersistencePort;
    private final RolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists("cbarrios", "Carlos", "Barrios", "+16723381837", "cbarrios", Set.of(RoleType.ROLE_SUPERUSER));
        createUserIfNotExists("owner", "Owner", "User", "+16723381838", "owner", Set.of(RoleType.ROLE_OWNER));
        createUserIfNotExists("manager", "Manager", "User", "+16723381839", "manager", Set.of(RoleType.ROLE_MANAGER));
        createUserIfNotExists("waiter", "Waiter", "User", "+16723381840", "waiter", Set.of(RoleType.ROLE_WAITER));
        createUserIfNotExists("chef", "Chef", "User", "+16723381841", "chef", Set.of(RoleType.ROLE_CHEF));
        createUserIfNotExists("kitchenstaff", "Kitchen", "Staff", "+16723381842", "kitchenstaff", Set.of(RoleType.ROLE_KITCHEN_STAFF));
        createUserIfNotExists("cashier", "Cashier", "User", "+16723381843", "cashier", Set.of(RoleType.ROLE_CASHIER));
    }

    private void createUserIfNotExists(String username, String name, String lastname, String phone, String password, Set<RoleType> roles) {
        if (userPersistencePort.findUserByUsername(username).isEmpty()) {
            Role role = roles.stream()
                    .map(rolePersistencePort::findByName)
                    .map(Optional::get) // Using get() here safely since we will check for existence below
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("Critical error: A role does not exist. Please create it before running the application.");
                        return new IllegalStateException("Role not found.");
                    });

            User user = User.builder()
                    .name(name)
                    .lastname(lastname)
                    .phone(phone)
                    .email(name.toLowerCase() + "@example.com") // Asignar un email único basado en el nombre
                    .username(username)
                    .encodedPassword(passwordEncoder.encode(password))
                    .roles(Set.of(role)) // Assign the found role
                    .build();

            userPersistencePort.createUser(user);
            log.info("User '" + username + "' created successfully with role: " + role.roleType().getAuthority());
        } else {
            log.info("User '" + username + "' already exists.");
        }
    }
}
