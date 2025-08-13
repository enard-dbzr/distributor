package com.plux.distribution.domain.message.participant;

public record UnknownServiceParticipant() implements Participant{

    @Override
    public void accept(ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
