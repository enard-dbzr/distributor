package com.plux.distribution.core.message.domain.participant;

import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public record ServiceParticipant(@NotNull ServiceId serviceId) implements Participant {

    @Override
    public <R> R accept(@NotNull ParticipantVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
