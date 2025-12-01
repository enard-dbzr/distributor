package com.plux.distribution.core.chat.application.port.in;

import com.plux.distribution.core.chat.application.dto.ChatDto;
import org.jetbrains.annotations.NotNull;

public interface CreateChatUseCase {

    @NotNull ChatDto create();
}
