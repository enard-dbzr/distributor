package com.plux.distribution.domain.message.state;

import java.util.Date;

public record ReceivedState(Date receiveTime) implements MessageState {

    @Override
    public <R> R accept(MessageStateVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
