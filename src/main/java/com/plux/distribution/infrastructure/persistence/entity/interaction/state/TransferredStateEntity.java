package com.plux.distribution.infrastructure.persistence.entity.interaction.state;

import com.plux.distribution.core.interaction.domain.InteractionState;
import com.plux.distribution.core.interaction.domain.InteractionState.Transferred;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("TRANSFERRED")
public class TransferredStateEntity extends InteractionStateEntity {

    private Date transferTime;

    public TransferredStateEntity(Date transferTime) {
        this.transferTime = transferTime;
    }

    public TransferredStateEntity() {

    }

    @Override
    public InteractionState toModel() {
        return new Transferred(transferTime);
    }
}
