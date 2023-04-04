package com.cms.auth.utills;

import com.cms.auth.enums.ErrorResponseStatus;
import com.cms.auth.wrapper.ErrorResponseWrapper;
import com.cms.auth.wrapper.ResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FilterErrorResponseGenerator {

    public void sendErrorResponse(HttpServletResponse response, ErrorResponseStatus responseStatus,
                                  HttpStatus httpStatus) throws IOException {
        ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper(responseStatus, null);
        byte[] responseToSend = restResponseBytes(errorResponseWrapper);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(httpStatus.value());
        response.getOutputStream().write(responseToSend);
    }


    /**
     * Generate a response
     *
     * @param responseWrapper responseWrapper
     * @return response
     * @throws IOException IOException
     */
    private byte[] restResponseBytes(ResponseWrapper responseWrapper) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(responseWrapper);
        return serialized.getBytes();
    }
}
