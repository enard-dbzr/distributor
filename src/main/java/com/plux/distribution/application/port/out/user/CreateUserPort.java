package com.plux.distribution.application.port.out.user;

import com.plux.distribution.domain.user.User;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public interface CreateUserPort {
    @NotNull UserId create(@NotNull User user);
}
