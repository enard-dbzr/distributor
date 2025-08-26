package com.plux.distribution.application.port.in.chat;

import com.plux.distribution.domain.chat.ChatId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GetAllChatsUseCase {
    @NotNull List<ChatId> getAllChatIds();
}
