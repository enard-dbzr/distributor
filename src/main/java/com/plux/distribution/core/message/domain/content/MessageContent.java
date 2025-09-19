package com.plux.distribution.core.message.domain.content;

public sealed interface MessageContent permits ReplyMessageContent, SimpleMessageContent {

    <R> R accept(MessageContentVisitor<R> visitor);
}
