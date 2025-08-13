package com.plux.distribution.domain.message.attachment;

public record ButtonAttachment(String text, String tag) implements MessageAttachment {

    @Override
    public void accept(AttachmentVisitor visitor) {
        visitor.visit(this);
    }
}
