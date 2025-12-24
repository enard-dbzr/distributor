package com.plux.distribution.infrastructure.persistence.entity.message.state;

import com.plux.distribution.core.message.domain.state.MessageState;
import com.plux.distribution.core.message.domain.state.TransferredState;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("TRANSFERRED")
public class TransferredStateEntity extends MessageStateEntity {
    private Date transferTime;

    @Override
    public MessageState toModel() {
        return new TransferredState(transferTime);
    }

    public static TransferredStateEntity fromModel(TransferredState model) {
        var entity = new TransferredStateEntity();

        entity.transferTime = model.transferTime();

        return entity;
    }
}
