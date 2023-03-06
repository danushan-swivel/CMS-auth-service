package com.cms.auth.wrapper;

import com.cms.auth.domain.response.ResponseDto;
import com.cms.auth.enums.ErrorResponseStatus;
import com.cms.auth.enums.SuccessResponseStatus;

public class ErrorResponseWrapper extends ResponseWrapper{
    public ErrorResponseWrapper(ErrorResponseStatus errorResponseStatus, ResponseDto data) {
        super(errorResponseStatus.getMessage(), errorResponseStatus.getStatusCode(), data);
    }
}
