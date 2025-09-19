package com.plux.distribution.core.message.domain.state;

public record PendingState() implements MessageState {

    @Override
    public <R> R accept(MessageStateVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
