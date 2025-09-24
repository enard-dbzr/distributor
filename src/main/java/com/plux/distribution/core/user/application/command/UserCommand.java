package com.plux.distribution.core.user.application.command;

import com.plux.distribution.core.user.domain.UserInfo;

public record UserCommand(
        UserInfo userInfo
) {

}
