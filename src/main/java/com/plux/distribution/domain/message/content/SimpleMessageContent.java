package com.plux.distribution.domain.message.content;

import com.plux.distribution.domain.message.attachment.MessageAttachment;
import java.util.List;

public record SimpleMessageContent(String text, List<MessageAttachment> attachments) implements
        MessageContent {

    @Override
    public void accept(MessageContentVisitor visitor) {
        visitor.visit(this);
    }
}
