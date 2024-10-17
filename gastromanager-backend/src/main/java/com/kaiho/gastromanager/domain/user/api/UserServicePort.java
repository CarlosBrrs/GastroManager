package com.kaiho.gastromanager.domain.user.api;

import com.kaiho.gastromanager.domain.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServicePort extends UserDetailsService {

    User createUser(User domainProvisional);
}
