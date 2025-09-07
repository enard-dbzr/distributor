package com.plux.distribution.domain.message.content;

public sealed interface MessageContent permits SimpleMessageContent {

    <R> R accept(MessageContentVisitor<R> visitor);
}
