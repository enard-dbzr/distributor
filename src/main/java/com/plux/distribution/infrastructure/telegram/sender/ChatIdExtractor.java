package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.domain.message.participant.ParticipantVisitor;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

class ChatIdExtractor implements ParticipantVisitor {

    private ChatId chatId;

    @Override
    public void visit(ServiceParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to chats via telegram is not supported");
    }

    @Override
    public void visit(UnknownServiceParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to chats via telegram is not supported");
    }

    @Override
    public void visit(ChatParticipant participant) {
        chatId = participant.chatId();
    }

    @Override
    public void visit(SelfParticipant participant) {
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