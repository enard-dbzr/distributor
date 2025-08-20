package com.plux.distribution.application.service;

import com.plux.distribution.application.dto.user.CreateUserCommand;
import com.plux.distribution.application.dto.user.UserDto;
import com.plux.distribution.application.port.in.user.CreateUserUseCase;
import com.plux.distribution.application.port.out.user.CreateUserPort;
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
