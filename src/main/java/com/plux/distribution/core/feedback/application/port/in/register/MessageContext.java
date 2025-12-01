package com.plux.distribution.core.feedback.application.port.in.register;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.application.exception.ChatIdNotFound;
import com.plux.distribution.core.interaction.domain.InteractionId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public interface MessageContext {

    @NotNull ChatId getChatId() throws ChatIdNotFound;

    InteractionId getReplyTo();

    @NotNull String getText();

    @NotNull Date getTimestamp();

    void onMessageCreated(@NotNull InteractionId interactionId);

    void onChatCreated(@NotNull ChatId chatId);
}
