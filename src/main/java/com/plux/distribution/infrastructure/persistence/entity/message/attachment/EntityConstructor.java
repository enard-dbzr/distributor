package com.plux.distribution.infrastructure.persistence.entity.message.attachment;

import com.plux.distribution.domain.message.attachment.AttachmentVisitor;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;

class EntityConstructor implements AttachmentVisitor {
    AttachmentEntity result;

    @Override
    public void visit(ButtonAttachment attachment) {
        result = ButtonAttachmentEntity.fromModel(attachment);
    }
}
