package com.plux.distribution.application.dto.feedback;

import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.chat.ChatId;
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
