package com.cms.auth.exception;

public class AlreadyExistException extends AuthException {
    public AlreadyExistException(String errorMessage) {
        super(errorMessage);
    }

    public AlreadyExistException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
