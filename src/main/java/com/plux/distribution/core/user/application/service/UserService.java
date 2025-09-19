package com.plux.distribution.core.user.application.service;

import com.plux.distribution.core.user.application.command.CreateUserCommand;
import com.plux.distribution.core.user.application.dto.UserDto;
import com.plux.distribution.core.user.application.port.in.CreateUserUseCase;
import com.plux.distribution.core.user.application.port.out.CreateUserPort;
import org.jetbrains.annotations.NotNull;

public class UserService implements CreateUserUseCase {
    private final @NotNull CreateUserPort createUserPort;

    public UserService(@NotNull CreateUserPort createUserPort) {
        this.createUserPort = createUserPort;
    }

    @Override
    public @NotNull UserDto create(@NotNull CreateUserCommand command) {
        return new UserDto(createUserPort.create(command));
    }
}
