package com.plux.distribution.core.message.domain.participant;

public interface ParticipantVisitor<R> {
    R visit(ServiceParticipant participant);
    R visit(UnknownServiceParticipant participant);
    R visit(ChatParticipant participant);
    R visit(SelfParticipant participant);
}
