package com.plux.distribution.domain.message.participant;

import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public record ServiceParticipant(@NotNull ServiceId serviceId) implements Participant {

    @Override
    public <R> R accept(@NotNull ParticipantVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
