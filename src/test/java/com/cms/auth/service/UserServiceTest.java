package com.cms.auth.service;

import com.cms.auth.domain.entity.User;
import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.enums.RoleType;
import com.cms.auth.exception.AlreadyExistException;
import com.cms.auth.exception.AuthException;
import com.cms.auth.exception.InvalidUserException;
import com.cms.auth.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    private static final String USER_ID = "uid-7589=3695=8547=158";
    private static final String USER_NAME = "Danushan";
    private static final String PASSWORD = "test123";
    private static final String EMAIL = "Danushan@gmail.com";
    private static final String ROLE = "ADMIN";
    private static final String ENCODED_PASSWORD = "$2a$10$m1fdnChb14FY1thd4NONBePctp8cKFQPmu6B5VAgHQdPCq363JYwC";
    private static final int PAGE = 0;
    private static final int SIZE = 100;
    private static final String DEFAULT_SORT = "updated_at";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnUser_When_CreateUserSuccessfully() {
        User user = getSampleUser();
        UserRequestDto userRequestDto = getSampleUserRequestDto();
        when(userRepository.existsByUserNameAndIsDeletedFalse(USER_NAME)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);
        assertEquals(user, userService.createUser(userRequestDto));
    }

    @Test
    void Should_ThrowAlreadyExistException_When_UserNameAlreadyExistsInDatabase() {
        UserRequestDto userRequestDto = getSampleUserRequestDto();
        when(userRepository.existsByUserNameAndIsDeletedFalse(USER_NAME)).thenReturn(true);
        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () ->
                userService.createUser(userRequestDto));
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void Should_ThrowAuthException_When_CheckUserNameInDatabaseIsFailed() {
        UserRequestDto userRequestDto = getSampleUserRequestDto();
        when(userRepository.existsByUserNameAndIsDeletedFalse(USER_NAME)).thenThrow(new DataAccessException("ERROR") {
        });
        AuthException exception = assertThrows(AuthException.class, () ->
                userService.createUser(userRequestDto));
        assertEquals("Checking existence of user name is failed", exception.getMessage());
    }

    @Test
    void Should_ThrowAuthException_When_SaveNewUserIntoDatabaseIsFailed() {
        UserRequestDto userRequestDto = getSampleUserRequestDto();
        when(userRepository.existsByUserNameAndIsDeletedFalse(USER_NAME)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("ERROR") {
        });
        AuthException exception = assertThrows(AuthException.class, () ->
                userService.createUser(userRequestDto));
        assertEquals("Saving user in database is failed", exception.getMessage());
    }

    @Test
    void Should_ReturnUser_When_ValidUserNameIsProvidedForLogin() {
        User user = getSampleUser();
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.of(user));
        assertEquals(user, userService.login(USER_NAME));
    }

    @Test
    void Should_ThrowInvalidUserException_When_InvalidUserNameIsProvidedForLogin() {
        when(userRepository.findByUserName(USER_NAME)).thenReturn(Optional.empty());
        InvalidUserException exception = assertThrows(InvalidUserException.class, () ->
                userService.login(USER_NAME));
        assertEquals("The user name not exists", exception.getMessage());
    }

    @Test
    void Should_ThrowAuthException_When_InvalidUserNameIsProvidedForLogin() {
        when(userRepository.findByUserName(USER_NAME)).thenThrow(new DataAccessException("ERROR") {
        });
        AuthException exception = assertThrows(AuthException.class, () ->
                userService.login(USER_NAME));
        assertEquals("Getting user from database is failed", exception.getMessage());
    }

    @Test
    void Should_ReturnUserPage() {
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(DEFAULT_SORT).descending());
        Page<User> userPage = getSampleUserPage();
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        assertEquals(userPage, userService.getAllUser());
    }

    @Test
    void Should_ThrowAuthException_When_GetAllDetailsFromDatabaseIsFailed() {
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(DEFAULT_SORT).descending());
        when(userRepository.findAll(pageable)).thenThrow(new DataAccessException("ERROR") {
        });
        AuthException exception = assertThrows(AuthException.class, () ->
                userService.getAllUser());
        assertEquals("Getting all user from database is failed", exception.getMessage());
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