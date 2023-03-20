package com.cms.auth.exception;

public class InvalidUserException extends AuthException {
    public InvalidUserException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidUserException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
