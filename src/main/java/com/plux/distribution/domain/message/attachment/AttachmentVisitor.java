package com.plux.distribution.domain.message.attachment;

public interface AttachmentVisitor<R> {
    R visit(ButtonAttachment attachment);
}
