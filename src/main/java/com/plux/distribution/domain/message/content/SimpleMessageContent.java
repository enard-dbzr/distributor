package com.plux.distribution.domain.message.content;

import com.plux.distribution.domain.message.attachment.MessageAttachment;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record SimpleMessageContent(@NotNull String text, @NotNull List<MessageAttachment> attachments) implements
        MessageContent {

    @Override
    public void accept(@NotNull MessageContentVisitor visitor) {
        visitor.visit(this);
    }
}
