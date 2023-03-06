package com.cms.auth.service;

import com.cms.auth.domain.entity.User;
import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.exception.AlreadyExistException;
import com.cms.auth.exception.AuthException;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public boolean isUserExists(String userName) {
        try {
            return userRepository.existsByUserNameAndIsDeletedFalse(userName);
        } catch (DataAccessException e) {
            throw new AuthException("Checking existence of user name is failed", e);
        }
    }

    public User login(String userName) {
        try {
            return userRepository.findByUserName(userName);
        } catch (DataAccessException e) {
            throw new AuthException("Getting user from database is failed", e);
        }
    }

    public Page<User> getAllUser() {
        try {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("updated_at").descending());
            return userRepository.findAll(pageable);
        } catch (DataAccessException e) {
            throw new AuthException("Getting all user from database is failed", e);
        }
    }
}
