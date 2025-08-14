package com.plux.distribution.infrastructure.persistence.entity.message.content;

import com.plux.distribution.domain.message.content.MessageContentVisitor;
import com.plux.distribution.domain.message.content.SimpleMessageContent;

class EntityConstructor implements MessageContentVisitor {
    MessageContentEntity result;

    @Override
    public void visit(SimpleMessageContent content) {
        result = SimpleContentEntity.fromModel(content);
    }
}
