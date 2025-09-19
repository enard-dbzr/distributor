package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.core.message.domain.participant.ParticipantVisitor;
import com.plux.distribution.core.message.domain.participant.SelfParticipant;
import com.plux.distribution.core.message.domain.participant.ServiceParticipant;
import com.plux.distribution.core.message.domain.participant.UnknownServiceParticipant;
import com.plux.distribution.core.message.domain.participant.ChatParticipant;
import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

class ChatIdExtractor implements ParticipantVisitor<Void> {

    private ChatId chatId;

    @Override
    public Void visit(ServiceParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to chats via telegram is not supported");
    }

    @Override
    public Void visit(UnknownServiceParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to chats via telegram is not supported");
    }

    @Override
    public Void visit(ChatParticipant participant) {
        chatId = participant.chatId();
        return null;
    }

    @Override
    public Void visit(SelfParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to chats via telegram is not supported");
    }

    public @NotNull ChatId getChatId() {
        if (chatId == null) {
            throw new IllegalStateException("Chat not set");
        }
        return chatId;
    }
}