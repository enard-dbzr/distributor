package com.plux.distribution.domain.message.attachment;

public interface AttachmentVisitor {
    void visit(ButtonAttachment attachment);
}
