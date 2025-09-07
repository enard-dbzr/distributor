package com.plux.distribution.domain.message.participant;

public sealed interface Participant
        permits ChatParticipant, SelfParticipant, ServiceParticipant, UnknownServiceParticipant {

    <R> R accept(ParticipantVisitor<R> visitor);

    boolean equals(Object other);
}
