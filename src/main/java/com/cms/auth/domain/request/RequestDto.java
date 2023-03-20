package com.cms.auth.domain.request;

import com.cms.auth.exception.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class RequestDto {
    public boolean isRequiredAvailable() {
        return true;
    }

    public boolean isNonEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new AuthException("Convert object to string is failed", e);
        }
    }
}
