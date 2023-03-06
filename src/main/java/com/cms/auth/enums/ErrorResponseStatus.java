package com.cms.auth.enums;

import lombok.Getter;

@Getter
public enum ErrorResponseStatus {
    INTERNAL_SERVER_ERROR(5000, "Internal server error"),
    MISSING_REQUIRED_FIELDS(4000, "The required fields are missing"),
    INVALID_USER(3050, "The use is invalid"),
    USER_EXISTS(3051, "The user already exists");

    private final String message;
    private final int statusCode;

    ErrorResponseStatus(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
