package com.kaiho.gastromanager.domain.auth.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class VerificationTokenDoesNotExistException extends EntityDoesNotExistException {
    public VerificationTokenDoesNotExistException(String email) {
        super("Verification token for email: " + email + " does not exist");
    }
}
