package com.plux.distribution.domain.message.state;

public sealed interface MessageState permits PendingState, ReceivedState, TransferredState {

    void accept(MessageStateVisitor visitor);

    boolean equals(Object other);
}
