package com.plux.distribution.application.port.out.specific.telegram.chat;

import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public interface GetChatIdByTgPort {

    @NotNull ChatId getChatId(@NotNull Long tgUserId) throws ChatIdNotFound;
}
