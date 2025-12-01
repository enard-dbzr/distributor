package com.plux.distribution.infrastructure.persistence.entity.interaction.state;

import com.plux.distribution.core.interaction.domain.InteractionState;
import com.plux.distribution.core.interaction.domain.InteractionState.Pending;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PENDING")
public class PendingStateEntity extends InteractionStateEntity {

    @Override
    public InteractionState toModel() {
        return new Pending();
    }
}
