package com.kaiho.gastromanager.domain.auth.exception;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;

public class TokenDoesNotExistException extends EntityDoesNotExistException {
    public TokenDoesNotExistException(String token) {
        super("Token with UUID " + token + " does not exist");
    }
}
