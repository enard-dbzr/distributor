package com.plux.distribution.domain.action;

import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.message.MessageId;

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
