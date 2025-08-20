package com.plux.distribution.application.port.out.user;

import com.plux.distribution.application.dto.user.CreateUserCommand;
import com.plux.distribution.domain.user.User;

public interface CreateUserPort {
    User create(CreateUserCommand createUserCommand);
}
