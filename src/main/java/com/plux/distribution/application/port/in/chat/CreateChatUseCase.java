package com.plux.distribution.application.port.in.chat;

import com.plux.distribution.application.dto.chat.ChatDto;
import org.jetbrains.annotations.NotNull;

public interface CreateChatUseCase {
    @NotNull ChatDto create();
}
