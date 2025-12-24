package com.plux.distribution.core.message.domain.participant;

import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public record ChatParticipant(@NotNull ChatId chatId) implements Participant {

    @Override
    public <R> R accept(@NotNull ParticipantVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
