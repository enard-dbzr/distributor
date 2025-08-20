package com.plux.distribution.application.dto.user;

import com.plux.distribution.domain.user.UserInfo;

public record CreateUserCommand (
        UserInfo userInfo
) {

}
