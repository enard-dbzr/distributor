package com.plux.distribution.infrastructure.persistence.entity.message.attachment;

import com.plux.distribution.core.message.domain.attachment.AttachmentVisitor;
import com.plux.distribution.core.message.domain.attachment.ButtonAttachment;
import com.plux.distribution.core.message.domain.attachment.MessageAttachment;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "attachments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private Long id;

    public static AttachmentEntity fromModel(MessageAttachment model) {
        //noinspection Convert2Lambda,Anonymous2MethodRef
        return model.accept(new AttachmentVisitor<>() {
            @Override
            public AttachmentEntity visit(ButtonAttachment attachment) {
                return ButtonAttachmentEntity.fromModel(attachment);
            }
        });
    }

    public abstract MessageAttachment toModel();
}