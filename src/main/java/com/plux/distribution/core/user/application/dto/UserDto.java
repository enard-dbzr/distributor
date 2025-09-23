package com.plux.distribution.core.user.application.dto;

import com.plux.distribution.core.user.domain.User;
import com.plux.distribution.core.user.domain.UserId;
import com.plux.distribution.core.user.domain.UserInfo;

public record UserDto(
        UserId id,
        UserInfo userInfo
) {
    public UserDto (User model) {
        this(model.getId(), model.getUserInfo());
    }
}
