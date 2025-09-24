package com.plux.distribution.core.user.application.port.in;

import com.plux.distribution.core.user.application.command.UserCommand;
import com.plux.distribution.core.user.application.dto.UserDto;
import org.jetbrains.annotations.NotNull;

public interface CreateUserUseCase {
    @NotNull UserDto create(@NotNull UserCommand command);
}
