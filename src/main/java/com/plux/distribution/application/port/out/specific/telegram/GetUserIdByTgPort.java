package com.plux.distribution.application.port.out.specific.telegram;

import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public interface GetUserIdByTgPort {

    @NotNull UserId getUserId(@NotNull Long tgUserId);
}
