package com.plux.distribution.domain.message.state;

public interface MessageStateVisitor {
    void visit(PendingState state);
    void visit(TransferredState state);
    void visit(ReceivedState state);
}
