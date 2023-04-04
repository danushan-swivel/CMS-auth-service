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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
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
                log.error("The required field values {} are missing for create new user", userRequestDto.toJson());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            var user = userService.createUser(userRequestDto);
            var response = new UserResponseDto(user);
            var wrapper = new SuccessResponseWrapper(SuccessResponseStatus.USER_CREATES, response);
            log.debug("Successfully created new user");
            return new ResponseEntity<>(wrapper, HttpStatus.OK);
        } catch (AlreadyExistException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.USER_EXISTS, null);
            log.error("The given user name: {} is already registered", userRequestDto.getUserName());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (AuthException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The create new user is failed for user name: {}", userRequestDto.getUserName());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(@RequestParam String username, @RequestParam String password,
                                                 HttpServletResponse response) {
        try {
            String token = response.getHeader(Constants.TOKEN_HEADER);
            User user = userService.login(username);
            var responseDto = new LoginResponseDto(user, token);
            var wrapper = new SuccessResponseWrapper(SuccessResponseStatus.USER_LOGGED_IN, responseDto);
            log.debug("The user logged in successfully");
            return new ResponseEntity<>(wrapper, HttpStatus.OK);
        } catch (AuthException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The logging in the user is failed for username: {}", username);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseWrapper> getAllUsers() {
        try {
            Page<User> userPage = userService.getAllUser();
            var response = new AllUserResponseDto(userPage);
            var wrapper = new SuccessResponseWrapper(SuccessResponseStatus.READ_LIST_USER, response);
            log.debug("Successfully returned the all registered users");
            return new ResponseEntity<>(wrapper, HttpStatus.OK);
        } catch (AuthException e) {
            var errorResponse = new ErrorResponseWrapper(ErrorResponseStatus.INTERNAL_SERVER_ERROR, null);
            log.error("The get the all user details is failed");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
