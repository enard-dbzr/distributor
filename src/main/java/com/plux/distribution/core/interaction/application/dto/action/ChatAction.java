package com.plux.distribution.core.interaction.application.dto.action;

import com.plux.distribution.core.chat.domain.ChatId;

public abstract class ChatAction {

    private final ChatId chatId;

    protected ChatAction(ChatId chatId) {
        this.chatId = chatId;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public abstract void accept(ChatActionVisitor visitor);
}
