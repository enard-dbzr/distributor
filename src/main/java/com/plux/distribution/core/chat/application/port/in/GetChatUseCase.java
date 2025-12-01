package com.plux.distribution.core.chat.application.port.in;

import com.plux.distribution.core.chat.application.dto.ChatDto;
import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public interface GetChatUseCase {

    @NotNull ChatDto get(@NotNull ChatId chatId);
}
