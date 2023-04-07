package com.cms.auth.controller;

import com.cms.auth.domain.entity.User;
import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.enums.ErrorResponseStatus;
import com.cms.auth.enums.RoleType;
import com.cms.auth.enums.SuccessResponseStatus;
import com.cms.auth.exception.AlreadyExistException;
import com.cms.auth.exception.AuthException;
import com.cms.auth.service.UserService;
import com.cms.auth.utills.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class tests {@link UserController} class
 */
class UserControllerTest {

    private static final String CREATE_USER_URL = "/api/v1/user/sign-up";
    private static final String LOGIN_USER_URL = "/api/v1/user/login";
    private static final String GET_ALL_USER_URL = "/api/v1/user/users";

    private static final String USER_ID = "uid-7589=3695=8547=158";
    private static final String USER_NAME = "Danushan";
    private static final String PASSWORD = "test123";
    private static final String EMAIL = "Danushan@gmail.com";
    private static final String ROLE = "ADMIN";
    private static final String ENCODED_PASSWORD = "$2a$10$m1fdnChb14FY1thd4NONBePctp8cKFQPmu6B5VAgHQdPCq363JYwC";
    private static final String ACCESS_TOKEN = "ey1365651-14156-51";

    @Mock
    private UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnOk_When_UserLoggedInSuccessfully() throws Exception {
        User user = getSampleUser();
        when(userService.login(anyString())).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_USER_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .param("username", USER_NAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.USER_LOGGED_IN.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.userId", startsWith("uid-")));
    }

    @Test
    void Should_ReturnOk_When_CreateUserSuccessfully() throws Exception {
        UserRequestDto userRequestDto = getSampleUserRequestDto();
        User user = getSampleUser();
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(userRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.USER_CREATES.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.data.userId", startsWith("uid-")));
    }

    @Test
    void Should_ReturnBadRequest_When_MissingRequiredFieldsForCreateUser() throws Exception {
        UserRequestDto userRequestDto = getSampleUserRequestDto();
        userRequestDto.setUserName("");
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .content(userRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatus.MISSING_REQUIRED_FIELDS.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.data", nullValue()));
    }

    @Test
    void Should_ReturnOk_When_GetAllUserDetailsSuccessfully() throws Exception {
        Page<User> userPage = getSampleUserPage();
        when(userService.getAllUser()).thenReturn(userPage);
        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_USER_URL)
                        .header(Constants.TOKEN_HEADER, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatus.READ_LIST_USER.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.users[0].userId", startsWith("uid-")));
    }


    /**
     * This method create sample user
     *
     * @return User
     */
    private User getSampleUser() {
        User user = new User();
        user.setUserId(USER_ID);
        user.setUserName(USER_NAME);
        user.setEmailAddress(EMAIL);
        user.setPassword(ENCODED_PASSWORD);
        user.setRoleType(RoleType.valueOf(ROLE));
        return user;
    }

    /**
     * This method creates sample user request dto
     *
     * @return UserRequestDto
     */
    private UserRequestDto getSampleUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserName(USER_NAME);
        userRequestDto.setEmailAddress(EMAIL);
        userRequestDto.setPassword(PASSWORD);
        userRequestDto.setRoleType(ROLE);
        return userRequestDto;
    }

    /**
     * This method creates sample user page
     *
     * @return UserPage
     */
    private Page<User> getSampleUserPage() {
        User user = getSampleUser();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        return new PageImpl<>(userList);
    }
}