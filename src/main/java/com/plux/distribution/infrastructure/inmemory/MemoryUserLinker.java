package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.exception.UserIdNotFound;
import com.plux.distribution.application.port.out.specific.telegram.GetTgUserIdPort;
import com.plux.distribution.application.port.out.specific.telegram.GetUserIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.TgUserLinker;
import com.plux.distribution.domain.user.UserId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class MemoryUserLinker implements GetTgUserIdPort, GetUserIdByTgPort, TgUserLinker {
    Map<Long, UserId> tgToUserId = new ConcurrentHashMap<>();
    Map<UserId, Long> userToTg = new ConcurrentHashMap<>();

    @Override
    public @NotNull Long getTgUserId(@NotNull UserId userId) {
        return userToTg.get(userId);
    }

    @Override
    public @NotNull UserId getUserId(@NotNull Long tgUserId) throws UserIdNotFound {
        if (!tgToUserId.containsKey(tgUserId)) {
            throw new UserIdNotFound("UserId not found");
        }

        return tgToUserId.get(tgUserId);
    }

    @Override
    public void link(@NotNull UserId internal, @NotNull Long external) {
        tgToUserId.put(external, internal);
        userToTg.put(internal, external);
    }
}
