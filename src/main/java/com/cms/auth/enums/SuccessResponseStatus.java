package com.cms.auth.enums;

import lombok.Getter;

@Getter
public enum SuccessResponseStatus {
    USER_CREATES("User created successfully"),
    USER_LOGGED_IN("User logging in successfully"),
    USER_UPDATED("User updated successfully"),
    USER_DELETED("User deleted successfully"),
    READ_USER("Read user successfully"),
    READ_LIST_USER("Read user list successfully");

    private final String message;

    SuccessResponseStatus(String message) {
        this.message = message;
    }
}
