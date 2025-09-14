package com.plux.distribution.domain.message.content;

public sealed interface MessageContent permits ReplyMessageContent, SimpleMessageContent {

    <R> R accept(MessageContentVisitor<R> visitor);
}
