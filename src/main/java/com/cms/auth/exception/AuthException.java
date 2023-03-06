package com.cms.auth.exception;

public class AuthException extends RuntimeException{
    public AuthException(String errorMessage) {
        super(errorMessage);
    }

    public AuthException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
