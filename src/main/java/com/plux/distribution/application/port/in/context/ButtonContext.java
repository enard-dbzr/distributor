package com.plux.distribution.application.port.in.context;

import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public interface ButtonContext {
    @NotNull ChatId getChatId() throws ChatIdNotFound;
    @NotNull MessageId getReplyTo();
    @NotNull String getTag();
}
