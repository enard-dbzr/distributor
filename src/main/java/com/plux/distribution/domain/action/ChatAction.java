package com.plux.distribution.domain.action;

import com.plux.distribution.domain.chat.ChatId;

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
