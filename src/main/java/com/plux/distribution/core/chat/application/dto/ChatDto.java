package com.plux.distribution.core.chat.application.dto;

import com.plux.distribution.core.chat.domain.Chat;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.user.domain.UserId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ChatDto(
        @NotNull ChatId id,
        @Nullable UserId userId
) {

    public ChatDto(@NotNull Chat model) {
        this(model.getId(), model.getUserId());
    }
}
