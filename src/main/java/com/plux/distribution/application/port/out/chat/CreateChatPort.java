package com.plux.distribution.application.port.out.chat;

import com.plux.distribution.domain.chat.Chat;
import org.jetbrains.annotations.NotNull;

public interface CreateChatPort {
    @NotNull Chat create();
}
