package com.cms.auth.enums;

import lombok.Getter;

@Getter
public enum SuccessResponseStatus {
    USER_CREATES(3000, "User created successfully"),
    USER_UPDATED(3001, "User updated successfully"),
    USER_DELETED(3002, "User deleted successfully"),
    READ_USER(3003, "Read user successfully"),
    READ_LIST_USER(3004, "Read user list successfully");

    private final String message;
    private final int statusCode;

    SuccessResponseStatus(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
