package com.plux.distribution.core.user.application.service;

import com.plux.distribution.core.user.application.command.UserCommand;
import com.plux.distribution.core.user.application.dto.UserDto;
import com.plux.distribution.core.user.application.port.in.CreateUserUseCase;
import com.plux.distribution.core.user.application.port.in.UpdateUserInfoUseCase;
import com.plux.distribution.core.user.application.port.out.UserRepositoryPort;
import com.plux.distribution.core.user.domain.UserId;
import com.plux.distribution.core.user.domain.UserInfo;
import org.jetbrains.annotations.NotNull;

public class UserService implements CreateUserUseCase, UpdateUserInfoUseCase {

    private final @NotNull UserRepositoryPort userRepositoryPort;

    public UserService(@NotNull UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public @NotNull UserDto create(@NotNull UserCommand command) {
        return new UserDto(userRepositoryPort.create(command));
    }

    @Override
    public void update(@NotNull UserId userId, @NotNull UserInfo userInfo) {
        var user = userRepositoryPort.get(userId);
        user.setUserInfo(userInfo);
        userRepositoryPort.update(user);
    }
}
