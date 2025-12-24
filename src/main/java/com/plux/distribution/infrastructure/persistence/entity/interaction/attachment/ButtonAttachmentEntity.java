package com.plux.distribution.infrastructure.persistence.entity.interaction.attachment;

import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUTTON")
public class ButtonAttachmentEntity extends AttachmentEntity {

    private String text;
    private String tag;

    public ButtonAttachmentEntity(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }

    public ButtonAttachmentEntity() {

    }

    @Override
    public MessageAttachment toModel() {
        return new ButtonAttachment(text, tag);
    }
}
