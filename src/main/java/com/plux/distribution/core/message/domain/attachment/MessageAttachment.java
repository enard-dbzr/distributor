package com.plux.distribution.core.message.domain.attachment;

public sealed interface MessageAttachment permits ButtonAttachment {

    <R> R accept(AttachmentVisitor<R> visitor);
}
