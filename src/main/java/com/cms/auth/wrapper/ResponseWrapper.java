package com.cms.auth.wrapper;

import com.cms.auth.domain.response.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseWrapper {
    private String message;
    private int statusCode;
    private ResponseDto data;
}
