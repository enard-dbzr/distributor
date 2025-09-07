package com.plux.distribution.domain.message.content;

public interface MessageContentVisitor<R> {
    R visit(SimpleMessageContent content);
}
