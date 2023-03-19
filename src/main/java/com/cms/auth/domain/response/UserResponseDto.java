package com.cms.auth.domain.response;

import com.cms.auth.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto extends ResponseDto {
    private String userId;
    private String userName;
    private String roleType;
    private long createdAt;
    private long updatedAt;
    private boolean isDeleted;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.roleType = user.getRoleType().name();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.isDeleted = user.isDeleted();
    }
}
