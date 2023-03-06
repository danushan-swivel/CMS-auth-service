package com.cms.auth.wrapper;

import com.cms.auth.domain.response.ResponseDto;
import com.cms.auth.enums.SuccessResponseStatus;

public class SuccessResponseWrapper extends ResponseWrapper{
    public SuccessResponseWrapper(SuccessResponseStatus successResponseStatus, ResponseDto data) {
        super(successResponseStatus.getMessage(), successResponseStatus.getStatusCode(), data);
    }
}
