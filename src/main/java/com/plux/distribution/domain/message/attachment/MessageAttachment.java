package com.plux.distribution.domain.message.attachment;

public sealed interface MessageAttachment permits ButtonAttachment {

    <R> R accept(AttachmentVisitor<R> visitor);
}
