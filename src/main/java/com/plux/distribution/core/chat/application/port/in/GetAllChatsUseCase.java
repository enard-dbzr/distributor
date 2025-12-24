package com.plux.distribution.core.chat.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GetAllChatsUseCase {
    @NotNull List<ChatId> getAllChatIds();
}
