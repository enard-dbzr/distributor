package com.plux.distribution.core.message.domain.content;

import com.plux.distribution.core.message.domain.MessageId;
import org.jetbrains.annotations.NotNull;

public record ReplyMessageContent(
        @NotNull MessageContent original,
        @NotNull MessageId replyTo
        ) implements MessageContent {

    @Override
    public <R> R accept(MessageContentVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
