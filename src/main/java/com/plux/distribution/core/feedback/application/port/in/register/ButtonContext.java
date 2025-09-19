package com.plux.distribution.core.feedback.application.port.in.register;

import com.plux.distribution.core.feedback.application.exception.ChatIdNotFound;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public interface ButtonContext {
    @NotNull ChatId getChatId() throws ChatIdNotFound;
    @NotNull MessageId getReplyTo();
    @NotNull String getTag();
}
