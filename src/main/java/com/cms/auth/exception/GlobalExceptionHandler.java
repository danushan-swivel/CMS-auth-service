package com.cms.auth.exception;

import com.cms.auth.enums.ErrorResponseStatus;
import com.cms.auth.wrapper.ErrorResponseWrapper;
import com.cms.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This method handle invalid user exception response
     *
     * @param exception invalid user exception
     * @return ErrorResponse/BadRequest
     */
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ResponseWrapper> invalidUserException(InvalidUserException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.INVALID_USER, HttpStatus.BAD_REQUEST);
        log.error("The provided user name not exists. Error message: {}", exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method handle already user exists exception response
     *
     * @param exception already user exist exception
     * @return ErrorResponse/BadRequest
     */
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ResponseWrapper> alreadyExistException(AlreadyExistException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.USER_EXISTS, HttpStatus.BAD_REQUEST);
        log.error("The provided user name already exists. Error message: {}", exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method handle the auth service exception response
     *
     * @param exception auth service exception
     * @return ErrorResponse/InternalServerError
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ResponseWrapper> alreadyExistException(AuthException exception) {
        var wrapper = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("The user service is failed. Error message: {}", exception.getMessage());
        return new ResponseEntity<>(wrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
