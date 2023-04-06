package com.cms.auth.enums;

import lombok.Getter;

@Getter
public enum ErrorResponseStatus {
    INTERNAL_SERVER_ERROR("Internal server error"),
    UNAUTHORIZED("The request is unauthorized"),
    MISSING_REQUIRED_FIELDS("The required fields are missing"),
    INVALID_USER("The use is invalid"),
    USER_EXISTS("The user already exists");

    private final String message;

    ErrorResponseStatus(String message) {
        this.message = message;
    }
}
