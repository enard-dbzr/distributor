package com.plux.distribution.core.message.domain.attachment;

public interface AttachmentVisitor<R> {
    R visit(ButtonAttachment attachment);
}
