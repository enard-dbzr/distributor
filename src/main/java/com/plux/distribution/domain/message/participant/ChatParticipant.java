package com.plux.distribution.domain.message.participant;

import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public record ChatParticipant(@NotNull ChatId chatId) implements Participant {

    @Override
    public void accept(@NotNull ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
