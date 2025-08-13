package com.plux.distribution.domain.message.participant;

import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public record ServiceParticipant(@NotNull ServiceId serviceId) implements Participant {

    @Override
    public void accept(@NotNull ParticipantVisitor visitor) {
        visitor.visit(this);
    }
}
