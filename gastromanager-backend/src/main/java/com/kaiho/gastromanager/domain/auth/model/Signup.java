package com.kaiho.gastromanager.domain.auth.model;

import com.kaiho.gastromanager.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Signup {
    private User user;
    /*    private final String name;
        private final String lastname;
        private final String username;
        private final String encodedPassword;*/
//    private final Contact contact;
    private Subscription subscription;

}
