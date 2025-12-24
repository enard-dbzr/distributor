package com.plux.distribution.core.message.domain.state;

public sealed interface MessageState permits PendingState, ReceivedState, TransferredState {

    <R> R accept(MessageStateVisitor<R> visitor);

    boolean equals(Object other);
}
