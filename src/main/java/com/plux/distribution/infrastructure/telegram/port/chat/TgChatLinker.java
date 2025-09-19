package com.plux.distribution.infrastructure.telegram.port.chat;

import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public interface TgChatLinker {
    void link(@NotNull ChatId internal, @NotNull Long external);
}
