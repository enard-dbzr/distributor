package com.plux.distribution.core.message.domain.attachment;

import org.jetbrains.annotations.NotNull;

public record ButtonAttachment(@NotNull String text, @NotNull String tag) implements MessageAttachment {

    @Override
    public <R> R accept(AttachmentVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
