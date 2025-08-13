package com.plux.distribution.domain.message.participant;

import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public record UserParticipant(@NotNull UserId userId) implements Participant {

    @Override
    public void accept(@NotNull ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
