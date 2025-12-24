package com.plux.distribution.core.message.domain.participant;

import org.jetbrains.annotations.NotNull;

public record SelfParticipant() implements Participant {

    @Override
    public <R> R accept(@NotNull ParticipantVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
