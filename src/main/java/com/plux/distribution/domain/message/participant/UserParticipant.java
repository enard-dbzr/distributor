package com.plux.distribution.domain.message.participant;

import com.plux.distribution.domain.user.UserId;

public record UserParticipant(UserId userId) implements Participant {

    @Override
    public void accept(ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
