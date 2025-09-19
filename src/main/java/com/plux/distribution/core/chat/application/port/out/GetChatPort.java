package com.plux.distribution.core.chat.application.port.out;

import com.plux.distribution.core.chat.domain.Chat;
import com.plux.distribution.core.chat.domain.ChatId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GetChatPort {

    @NotNull Chat get(@NotNull ChatId id);

    @NotNull List<ChatId> getAllChatIds();
}
