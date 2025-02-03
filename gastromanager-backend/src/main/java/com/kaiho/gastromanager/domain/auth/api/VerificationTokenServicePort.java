package com.kaiho.gastromanager.domain.auth.api;

import com.kaiho.gastromanager.domain.auth.model.VerificationToken;
import com.kaiho.gastromanager.domain.user.model.User;

public interface VerificationTokenServicePort {
    VerificationToken findByToken(String token);

    void deleteToken(VerificationToken verificationToken);


    void createVerificationToken(User createdUser);

    boolean isVerificationPending(User user);
}
