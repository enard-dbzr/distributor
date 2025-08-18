package com.plux.distribution.application.port.out.chat;

import com.plux.distribution.domain.chat.Chat;
import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public interface CreateChatPort {
    @NotNull ChatId create(@NotNull Chat chat);
}
