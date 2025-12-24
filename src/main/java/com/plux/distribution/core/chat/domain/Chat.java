package com.plux.distribution.core.chat.domain;

import com.plux.distribution.core.user.domain.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Chat {
    private final @NotNull ChatId id;
    private @Nullable UserId userId;

    public Chat(@NotNull ChatId id, @Nullable UserId userId) {
        this.id = id;
        this.userId = userId;
    }

    public @NotNull ChatId getId() {
        return id;
    }

    public @Nullable UserId getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UserId userId) {
        this.userId = userId;
    }
}
