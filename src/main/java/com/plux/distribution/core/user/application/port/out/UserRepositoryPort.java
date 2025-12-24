package com.plux.distribution.core.user.application.port.out;

import com.plux.distribution.core.user.application.command.UserCommand;
import com.plux.distribution.core.user.domain.User;
import com.plux.distribution.core.user.domain.UserId;
import org.jetbrains.annotations.NotNull;

public interface UserRepositoryPort {

    @NotNull User create(@NotNull UserCommand createUserCommand);

    void update(@NotNull User user);

    @NotNull User get(@NotNull UserId id);
}
