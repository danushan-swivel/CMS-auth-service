package com.cms.auth.domain.response;

import com.cms.auth.exception.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ResponseDto {
    public String toLogJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new AuthException("Convert object to string is failed", e);
        }
    }
}
