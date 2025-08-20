package com.plux.distribution.application.dto.user;

import com.plux.distribution.domain.user.User;
import com.plux.distribution.domain.user.UserId;
import com.plux.distribution.domain.user.UserInfo;

public record UserDto(
        UserId id,
        UserInfo userInfo
) {
    public UserDto (User model) {
        this(model.getUserId(), model.getUserInfo());
    }
}
