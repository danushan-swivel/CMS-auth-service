package com.cms.auth.controller;

import com.cms.auth.domain.entity.User;
import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.domain.response.AllUserResponseDto;
import com.cms.auth.domain.response.LoginResponseDto;
import com.cms.auth.domain.response.UserResponseDto;
import com.cms.auth.enums.ErrorResponseStatus;
import com.cms.auth.enums.SuccessResponseStatus;
import com.cms.auth.exception.AlreadyExistException;
import com.cms.auth.exception.AuthException;
import com.cms.auth.service.UserService;
import com.cms.auth.utills.Constants;
import com.cms.auth.wrapper.ErrorResponseWrapper;
import com.cms.auth.wrapper.ResponseWrapper;
import com.cms.auth.wrapper.SuccessResponseWrapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("api/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserRequestDto userRequestDto) {
        try {
            if (!userRequestDto.isRequiredAvailable()) {
                var response = new ErrorResponseWrapper(ErrorResponseStatus.MISSING_REQUIRED_FIELDS, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            var user = userService.createUser(userRequestDto);
            var response = new UserResponseDto(user);
            var wrapper = new SuccessResponseWrapper(SuccessResponseStatus.USER_CREATES, response);
            return new ResponseEntity<>(wrapper, HttpStatus.OK);
        } catch (AlreadyExistException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.USER_EXISTS, null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (AuthException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(Authentication authentication, HttpServletResponse response) {
        try {
            String token = response.getHeader(Constants.TOKEN_HEADER);
            User user = userService.login(authentication.getName());
            var responseDto = new LoginResponseDto(user, token);
            var wrapper = new SuccessResponseWrapper(SuccessResponseStatus.USER_LOGGING_IN, responseDto);
            return new ResponseEntity<>(wrapper, HttpStatus.OK);
        } catch (AuthException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseWrapper> getAllUsers() {
        try {
            Page<User> userPage = userService.getAllUser();
            var response = new AllUserResponseDto(userPage);
            var wrapper = new SuccessResponseWrapper(SuccessResponseStatus.READ_LIST_USER, response);
            return new ResponseEntity<>(wrapper, HttpStatus.OK);
        } catch (AuthException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
