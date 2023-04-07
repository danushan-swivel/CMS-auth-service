package com.cms.auth.controller;

import com.cms.auth.domain.entity.User;
import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.domain.response.AllUserResponseDto;
import com.cms.auth.domain.response.LoginResponseDto;
import com.cms.auth.domain.response.UserResponseDto;
import com.cms.auth.enums.ErrorResponseStatus;
import com.cms.auth.enums.SuccessResponseStatus;
import com.cms.auth.service.UserService;
import com.cms.auth.utills.Constants;
import com.cms.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequestMapping("api/v1/user")
@RestController
public class UserController extends BaseController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserRequestDto userRequestDto) {

        if (!userRequestDto.isRequiredAvailable()) {
            log.error("The required field values {} are missing for create new user", userRequestDto.toJson());
            return getErrorResponse(ErrorResponseStatus.MISSING_REQUIRED_FIELDS);
        }
        var user = userService.createUser(userRequestDto);
        var response = new UserResponseDto(user);
        log.debug("Successfully created new user");
        return getSuccessResponse(SuccessResponseStatus.USER_CREATES, response, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(@RequestParam String username, @RequestParam String password,
                                                 HttpServletResponse response) {

        String token = response.getHeader(Constants.TOKEN_HEADER);
        User user = userService.login(username);
        var responseDto = new LoginResponseDto(user, token);
        log.debug("The user logged in successfully");
        return getSuccessResponse(SuccessResponseStatus.USER_LOGGED_IN, responseDto, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseWrapper> getAllUsers() {

        Page<User> userPage = userService.getAllUser();
        var response = new AllUserResponseDto(userPage);
        log.debug("Successfully returned the all registered users");
        return getSuccessResponse(SuccessResponseStatus.READ_LIST_USER, response, HttpStatus.OK);
    }
}
