package com.plux.distribution.application.port.out.chat;

import com.plux.distribution.domain.chat.Chat;
import com.plux.distribution.domain.chat.ChatId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GetChatPort {

    @NotNull Chat get(@NotNull ChatId id);

    @NotNull List<ChatId> getAllChatIds();
}
