package com.cms.auth.wrapper;

import com.cms.auth.domain.response.ResponseDto;
import com.cms.auth.enums.SuccessResponseStatus;
import org.springframework.http.HttpStatus;

public class SuccessResponseWrapper extends ResponseWrapper{
    public SuccessResponseWrapper(SuccessResponseStatus successResponseStatus, ResponseDto data, HttpStatus httpStatus) {
        super(successResponseStatus.getMessage(), httpStatus.value(), data);
    }
}
