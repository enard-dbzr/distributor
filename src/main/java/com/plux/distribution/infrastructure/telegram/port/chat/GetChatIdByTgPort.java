package com.plux.distribution.infrastructure.telegram.port.chat;

import com.plux.distribution.core.feedback.application.exception.ChatIdNotFound;
import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public interface GetChatIdByTgPort {

    @NotNull ChatId getChatId(@NotNull Long tgChatId) throws ChatIdNotFound;
}
