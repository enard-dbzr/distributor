package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.user.CreateUserPort;
import com.plux.distribution.domain.user.User;
import com.plux.distribution.domain.user.UserId;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MemoryUserRepository implements CreateUserPort {
    private final List<User> users = new ArrayList<>();

    @Override
    public @NotNull UserId create(@NotNull User user) {
        users.add(user);
        return new UserId((long) (users.size() - 1));
    }
}
