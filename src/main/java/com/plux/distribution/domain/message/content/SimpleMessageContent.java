package com.plux.distribution.domain.message.content;

import com.plux.distribution.domain.message.attachment.MessageAttachment;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record SimpleMessageContent(@NotNull String text, @NotNull List<MessageAttachment> attachments) implements
        MessageContent {

    @Override
    public <R> R accept(@NotNull MessageContentVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
