package com.cms.auth.domain.response;

import com.cms.auth.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AllUserResponseDto extends ResponseDto{
    private final List<UserResponseDto> users;

    public AllUserResponseDto(Page<User> userPage) {
        this.users = convertToResponseDto(userPage);
    }

    private List<UserResponseDto> convertToResponseDto(Page<User> userPage) {
        return userPage.stream().map(UserResponseDto::new).collect(Collectors.toList());
    }
}
