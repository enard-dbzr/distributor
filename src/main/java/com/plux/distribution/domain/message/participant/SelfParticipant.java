package com.plux.distribution.domain.message.participant;

import org.jetbrains.annotations.NotNull;

public record SelfParticipant() implements Participant {

    @Override
    public void accept(@NotNull ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
