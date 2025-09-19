package com.plux.distribution.core.chat.application.port.out;

import com.plux.distribution.core.chat.domain.Chat;
import org.jetbrains.annotations.NotNull;

public interface UpdateChatPort {
    void update(@NotNull Chat chat);
}
