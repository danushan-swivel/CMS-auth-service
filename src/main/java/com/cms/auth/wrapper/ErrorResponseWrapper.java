package com.cms.auth.wrapper;

import com.cms.auth.domain.response.ResponseDto;
import com.cms.auth.enums.ErrorResponseStatus;

public class ErrorResponseWrapper extends ResponseWrapper {
    public ErrorResponseWrapper(ErrorResponseStatus errorResponseStatus, ResponseDto data) {
        super(errorResponseStatus.getMessage(), errorResponseStatus.getStatusCode(), data);
    }
}
