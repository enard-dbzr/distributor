package com.plux.distribution.domain.message.content;

public interface MessageContent {
    void accept(MessageContentVisitor visitor);
}
