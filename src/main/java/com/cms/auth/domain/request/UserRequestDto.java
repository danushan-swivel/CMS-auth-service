package com.cms.auth.domain.request;

import lombok.Getter;

@Getter
public class UserRequestDto extends RequestDto{
    private String userName;
    private String emailAddress;
    private String password;
    private String roleType;

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(userName) && isNonEmpty(password);
    }
}
