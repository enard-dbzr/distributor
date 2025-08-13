package com.plux.distribution.domain.message.participant;

import com.plux.distribution.domain.service.ServiceId;

public record ServiceParticipant(ServiceId serviceId) implements Participant {

    @Override
    public void accept(ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
