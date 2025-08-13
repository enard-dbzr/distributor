package com.plux.distribution.domain.message.state;

public record PendingState() implements MessageState {

    @Override
    public void accept(MessageStateVisitor visitor) {
        visitor.visit(this);
    }
}
