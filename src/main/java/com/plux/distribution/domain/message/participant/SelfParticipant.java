package com.plux.distribution.domain.message.participant;

public class SelfParticipant implements  Participant {

    @Override
    public void accept(ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
