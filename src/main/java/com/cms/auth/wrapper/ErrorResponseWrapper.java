package com.cms.auth.wrapper;

import com.cms.auth.enums.ErrorResponseStatus;
import org.springframework.http.HttpStatus;

public class ErrorResponseWrapper extends ResponseWrapper {
    public ErrorResponseWrapper(ErrorResponseStatus responseStatusMessage, HttpStatus httpStatus) {
        super(responseStatusMessage.getMessage(), httpStatus.value(), null);
    }
}
