package com.plux.distribution.infrastructure.persistence.entity.message.attachment;

import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUTTON")
public class ButtonAttachmentEntity extends AttachmentEntity {
    private String text;
    private String tag;


    public static ButtonAttachmentEntity fromModel(ButtonAttachment model) {
        var entity = new ButtonAttachmentEntity();

        entity.text = model.text();
        entity.tag = model.tag();

        return entity;
    }
}
