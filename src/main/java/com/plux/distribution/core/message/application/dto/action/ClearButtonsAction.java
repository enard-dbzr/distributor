package com.plux.distribution.core.message.application.dto.action;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.message.domain.MessageId;

public class ClearButtonsAction extends ChatAction {
    private final MessageId messageId;

    public ClearButtonsAction(ChatId chatId, MessageId messageId) {
        super(chatId);
        this.messageId = messageId;
    }

    public MessageId getMessageId() {
        return messageId;
    }

    @Override
    public void accept(ChatActionVisitor visitor) {
        visitor.visit(this);
    }
}
