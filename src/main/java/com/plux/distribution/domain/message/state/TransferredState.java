package com.plux.distribution.domain.message.state;

import java.util.Date;

public record TransferredState(Date transferTime) implements MessageState {

    @Override
    public void accept(MessageStateVisitor visitor) {
        visitor.visit(this);
    }
}
