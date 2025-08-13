package com.plux.distribution.domain.message.attachment;

public interface MessageAttachment {
    void accept(AttachmentVisitor visitor);
}
