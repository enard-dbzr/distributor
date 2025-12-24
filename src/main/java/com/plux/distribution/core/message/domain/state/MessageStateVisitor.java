package com.plux.distribution.core.message.domain.state;

public interface MessageStateVisitor<R> {
    R visit(PendingState state);
    R visit(TransferredState state);
    R visit(ReceivedState state);
}
