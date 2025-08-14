package com.plux.distribution.application.port.out.specific.telegram;

import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public interface GetTgUserIdPort {
    @NotNull Long getTgUserId(@NotNull UserId userId);
}
