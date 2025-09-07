package com.plux.distribution.domain.message.participant;

import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public record ChatParticipant(@NotNull ChatId chatId) implements Participant {

    @Override
    public <R> R accept(@NotNull ParticipantVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
