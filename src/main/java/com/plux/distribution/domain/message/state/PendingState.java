package com.plux.distribution.domain.message.state;

import org.jetbrains.annotations.NotNull;

public record PendingState() implements MessageState {

    @Override
    public void accept(@NotNull MessageStateVisitor visitor) {
        visitor.visit(this);
    }
}
