package com.plux.distribution.infrastructure.telegram.port;

import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TgChatLinker {

    void link(@NotNull ChatId internal, @NotNull Long external);

    @Nullable ChatId getChatId(@NotNull Long tgChatId);

    @NotNull Long getTgChatId(@NotNull ChatId chatId);
}
