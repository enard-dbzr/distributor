package com.plux.distribution.application.dto.chat;

import com.plux.distribution.domain.chat.Chat;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.user.UserId;
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
