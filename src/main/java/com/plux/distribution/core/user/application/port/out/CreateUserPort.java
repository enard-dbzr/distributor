package com.plux.distribution.core.user.application.port.out;

import com.plux.distribution.core.user.application.command.CreateUserCommand;
import com.plux.distribution.core.user.domain.User;

public interface CreateUserPort {
    User create(CreateUserCommand createUserCommand);
}
