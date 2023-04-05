package com.cms.auth.domain.response;

import com.cms.auth.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class LoginResponseDto extends ResponseDto{
    private String userId;
    private String token;
    private String userName;
    private String roleType;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;

    public LoginResponseDto(User user, String token) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.token = token;
        this.roleType = user.getRoleType().name();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.isDeleted = user.isDeleted();
    }
}
