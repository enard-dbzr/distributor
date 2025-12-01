package com.plux.distribution.core.feedback.application.port.in.register;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.application.exception.ChatIdNotFound;
import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public interface ButtonContext {

    @NotNull ChatId getChatId() throws ChatIdNotFound;

    @NotNull InteractionId getReplyTo();

    @NotNull String getTag();
}
