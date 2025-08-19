package com.plux.distribution.application.port.out.specific.telegram.chat;

import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public interface GetTgChatIdPort {
    @NotNull Long getTgChatId(@NotNull ChatId chatId);
}
