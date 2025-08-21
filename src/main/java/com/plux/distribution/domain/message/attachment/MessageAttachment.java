package com.plux.distribution.domain.message.attachment;

public sealed interface MessageAttachment permits ButtonAttachment {

    void accept(AttachmentVisitor visitor);
}
