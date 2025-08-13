package com.plux.distribution.domain.message.participant;

public interface Participant {
    void accept(ParticipantVisitor visitor);
}
