package com.cms.auth.domain.entity;

import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User {
    private static final String PRE_FIX = "uid-";
    @Id
    private String userId;
    private String emailAddress;
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    private long createdAt;
    private long updatedAt;
    private boolean isDeleted;

    public User(UserRequestDto userRequestDto) {
        this.userId = PRE_FIX + UUID.randomUUID();
        this.userName = userRequestDto.getUserName();
        this.emailAddress = userRequestDto.getEmailAddress();
        this.password = null;
        this.createdAt = this.updatedAt = System.currentTimeMillis();
        this.roleType = RoleType.valueOf(userRequestDto.getRoleType().toUpperCase());
        this.isDeleted = false;
    }

    public void update() {

    }
}
