package com.plux.distribution.application.port.out.user;

import com.plux.distribution.domain.user.User;
import com.plux.distribution.domain.user.UserInfo;

public interface CreateUserPort {
    User create(UserInfo userInfo);
}
