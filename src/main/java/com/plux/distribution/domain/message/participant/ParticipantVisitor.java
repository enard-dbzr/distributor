package com.plux.distribution.domain.message.participant;

public interface ParticipantVisitor {
    void visit(ServiceParticipant participant);
    void visit(UnknownServiceParticipant participant);
    void visit(UserParticipant participant);
    void visit(SelfParticipant participant);
}
