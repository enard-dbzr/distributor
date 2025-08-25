package com.plux.distribution.domain.message.participant;

public sealed interface Participant
        permits ChatParticipant, SelfParticipant, ServiceParticipant, UnknownServiceParticipant {

    void accept(ParticipantVisitor visitor);

    boolean equals(Object other);
}
