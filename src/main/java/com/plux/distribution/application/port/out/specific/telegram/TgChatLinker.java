package com.plux.distribution.application.port.out.specific.telegram;

import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public interface TgChatLinker {
    void link(@NotNull ChatId internal, @NotNull Long external);
}
