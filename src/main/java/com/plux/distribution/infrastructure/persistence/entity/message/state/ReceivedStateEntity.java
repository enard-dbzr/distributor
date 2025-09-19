package com.plux.distribution.infrastructure.persistence.entity.message.state;

import com.plux.distribution.core.message.domain.state.MessageState;
import com.plux.distribution.core.message.domain.state.ReceivedState;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("RECEIVED")
public class ReceivedStateEntity extends MessageStateEntity {
    private Date receiveTime;

    @Override
    public MessageState toModel() {
        return new ReceivedState(receiveTime);
    }

    public static ReceivedStateEntity fromModel(ReceivedState state) {
        var entity = new ReceivedStateEntity();

        entity.receiveTime = state.receiveTime();

        return entity;
    }
}
