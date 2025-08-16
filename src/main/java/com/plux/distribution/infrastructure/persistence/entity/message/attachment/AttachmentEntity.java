package com.plux.distribution.infrastructure.persistence.entity.message.attachment;

import com.plux.distribution.domain.message.attachment.AttachmentVisitor;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.attachment.MessageAttachment;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Table(name = "attachments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public static AttachmentEntity fromModel(MessageAttachment model) {
        var holder = new AtomicReference<AttachmentEntity>();

        model.accept(new AttachmentVisitor() {
            @Override
            public void visit(ButtonAttachment attachment) {
                holder.set(ButtonAttachmentEntity.fromModel(attachment));
            }
        });

        return holder.get();
    }
}