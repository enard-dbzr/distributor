package com.plux.distribution.domain.message.state;

import java.util.Date;

public record ReceivedState(Date receiveTime) implements MessageState {

    @Override
    public void accept(MessageStateVisitor visitor) {
        visitor.visit(this);
    }
}
