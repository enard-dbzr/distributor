package com.plux.distribution.domain.message.participant;

import org.jetbrains.annotations.NotNull;

public record UnknownServiceParticipant() implements Participant{

    @Override
    public <R> R accept(@NotNull ParticipantVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
