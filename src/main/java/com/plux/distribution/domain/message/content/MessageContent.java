package com.plux.distribution.domain.message.content;

public sealed interface MessageContent permits SimpleMessageContent {

    void accept(MessageContentVisitor visitor);
}
