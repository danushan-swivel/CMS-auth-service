package com.cms.auth.service;

import com.cms.auth.domain.entity.User;
import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.exception.AlreadyExistException;
import com.cms.auth.exception.AuthException;
import com.cms.auth.exception.InvalidUserException;
import com.cms.auth.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final int PAGE = 0;
    private static final int SIZE = 100;
    private static final String DEFAULT_SORT = "updated_at";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method create user
     *
     * @param userRequestDto user request dto
     * @return User
     */
    public User createUser(UserRequestDto userRequestDto) {
        try {
            if (isUserExists(userRequestDto.getUserName())) {
                throw new AlreadyExistException("User already exists");
            }
            User user = new User(userRequestDto);
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new AuthException("Saving user in database is failed", e);
        }
    }

    /**
     * This method check user existence
     *
     * @param userName user name
     * @return true/ false
     */
    public boolean isUserExists(String userName) {
        try {
            return userRepository.existsByUserNameAndIsDeletedFalse(userName);
        } catch (DataAccessException e) {
            throw new AuthException("Checking existence of user name is failed", e);
        }
    }

    /**
     * This method login the user
     *
     * @param userName user name
     * @return User
     */
    public User login(String userName) {
        try {
            var optionalUser = userRepository.findByUserName(userName);
            if (optionalUser.isEmpty()) {
                throw new InvalidUserException("The user name not exists");
            }
            return optionalUser.get();
        } catch (DataAccessException e) {
            throw new AuthException("Getting user from database is failed", e);
        }
    }

    /**
     * This method get all use details
     *
     * @return UserPage
     */
    public Page<User> getAllUser() {
        try {
            Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.by(DEFAULT_SORT).descending());
            return userRepository.findAll(pageable);
        } catch (DataAccessException e) {
            throw new AuthException("Getting all user from database is failed", e);
        }
    }
}
