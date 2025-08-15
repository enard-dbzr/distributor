package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.specific.telegram.GetTgUserIdPort;
import com.plux.distribution.application.port.out.specific.telegram.GetUserIdByTgPort;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public class ConstUserTgLinker implements GetTgUserIdPort, GetUserIdByTgPort {

    private final Long tgId;

    public ConstUserTgLinker(Long userId) {
        this.tgId = userId;
    }

    @Override
    public @NotNull Long getTgUserId(@NotNull UserId userId) {
        return tgId;
    }

    @Override
    public @NotNull UserId getUserId(@NotNull Long tgUserId) {
        return new UserId(1L);
    }
}
