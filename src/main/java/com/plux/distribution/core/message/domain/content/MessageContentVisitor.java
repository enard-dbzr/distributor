package com.plux.distribution.core.message.domain.content;

public interface MessageContentVisitor<R> {
    R visit(SimpleMessageContent content);
    R visit(ReplyMessageContent content);
}
