package com.plux.distribution.domain.message.state;

public interface MessageState {
    void accept(MessageStateVisitor visitor);
}
