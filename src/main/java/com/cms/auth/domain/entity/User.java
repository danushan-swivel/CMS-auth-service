package com.cms.auth.domain.entity;

import com.cms.auth.domain.request.UserRequestDto;
import com.cms.auth.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
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
    @Column(length = 50)
    private String userId;
    @Column(length = 40)
    private String emailAddress;
    @Column(length = 50)
    private String userName;
    @Column(length = 70)
    private String password;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;

    public User(UserRequestDto userRequestDto) {
        this.userId = PRE_FIX + UUID.randomUUID();
        this.userName = userRequestDto.getUserName();
        this.emailAddress = userRequestDto.getEmailAddress();
        this.password = null;
        this.createdAt = this.updatedAt = new Date(System.currentTimeMillis());
        this.roleType = RoleType.valueOf(userRequestDto.getRoleType().toUpperCase());
        this.isDeleted = false;
    }
}
