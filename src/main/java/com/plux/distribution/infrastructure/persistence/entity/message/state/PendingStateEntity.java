package com.plux.distribution.infrastructure.persistence.entity.message.state;

import com.plux.distribution.core.message.domain.state.MessageState;
import com.plux.distribution.core.message.domain.state.PendingState;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PENDING")
public class PendingStateEntity extends MessageStateEntity {

    @Override
    public MessageState toModel() {
        return new PendingState();
    }

    public static PendingStateEntity fromModel(@SuppressWarnings("unused") PendingState model) {
        return new PendingStateEntity();
    }
}
