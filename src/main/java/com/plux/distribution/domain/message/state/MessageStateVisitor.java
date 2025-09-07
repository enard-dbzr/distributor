package com.plux.distribution.domain.message.state;

public interface MessageStateVisitor<R> {
    R visit(PendingState state);
    R visit(TransferredState state);
    R visit(ReceivedState state);
}
