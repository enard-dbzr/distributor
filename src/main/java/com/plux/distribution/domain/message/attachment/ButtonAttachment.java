package com.plux.distribution.domain.message.attachment;

import org.jetbrains.annotations.NotNull;

public record ButtonAttachment(@NotNull String text, @NotNull String tag) implements MessageAttachment {

    @Override
    public void accept(@NotNull AttachmentVisitor visitor) {
        visitor.visit(this);
    }
}
