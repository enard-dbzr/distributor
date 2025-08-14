package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.domain.message.participant.ParticipantVisitor;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.UserParticipant;

class EntityConstructor implements ParticipantVisitor {
    ParticipantEntity result;

    @Override
    public void visit(ServiceParticipant participant) {
        result = ServiceParticipantEntity.fromModel(participant);
    }

    @Override
    public void visit(UnknownServiceParticipant participant) {
        result = UnkServiceParticipantEntity.fromModel(participant);
    }

    @Override
    public void visit(UserParticipant participant) {
        result = UserParticipantEntity.fromModel(participant);
    }

    @Override
    public void visit(SelfParticipant participant) {
        result = SelfParticipantEntity.fromModel(participant);
    }
}
