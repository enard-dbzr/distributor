package com.plux.distribution.domain.message.content;

public interface MessageContentVisitor {
    void visit(SimpleMessageContent content);
}
