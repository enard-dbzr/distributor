package com.plux.distribution.core.feedback.application.port.in.register;

import com.plux.distribution.core.feedback.application.exception.ChatIdNotFound;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.chat.domain.ChatId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public interface MessageContext {

    @NotNull ChatId getChatId() throws ChatIdNotFound;

    MessageId getReplyTo();

    @NotNull String getText();

    @NotNull Date getTimestamp();

    void onMessageCreated(@NotNull MessageId messageId);

    void onChatCreated(@NotNull ChatId chatId);
}
