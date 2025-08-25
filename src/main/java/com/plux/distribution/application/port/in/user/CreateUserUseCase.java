package com.plux.distribution.application.port.in.user;

import com.plux.distribution.application.dto.user.CreateUserCommand;
import com.plux.distribution.application.dto.user.UserDto;
import org.jetbrains.annotations.NotNull;

public interface CreateUserUseCase {
    @NotNull UserDto create(@NotNull CreateUserCommand command);
}
