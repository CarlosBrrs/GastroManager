package com.kaiho.gastromanager.domain.user.model;

import com.kaiho.gastromanager.domain.auth.model.Contact;
import com.kaiho.gastromanager.domain.restaurant.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public final class User implements UserDetails {
    private final UUID uuid;
    private String name;
    private String lastname;
    private Contact contact;
    private String username;
    private boolean verified;
    private String encodedPassword;
    private Set<Role> roles;
    private Restaurant restaurant;
    private List<Restaurant> restaurants;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.roleType().getAuthority()))
                    .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.encodedPassword;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
