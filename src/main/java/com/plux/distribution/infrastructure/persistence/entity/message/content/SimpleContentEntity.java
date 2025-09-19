package com.plux.distribution.infrastructure.persistence.entity.message.content;

import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.persistence.entity.message.attachment.AttachmentEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("SIMPLE")
public class SimpleContentEntity extends MessageContentEntity {

    private String text;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "content_id")
    private List<AttachmentEntity> attachments = new ArrayList<>();

    public static SimpleContentEntity fromModel(SimpleMessageContent model) {
        SimpleContentEntity entity = new SimpleContentEntity();

        entity.text = model.text();
        entity.attachments = model.attachments().stream().map(AttachmentEntity::fromModel).toList();

        return entity;
    }

    @Override
    public MessageContent toModel() {
        return new SimpleMessageContent(
                text,
                attachments.stream().map(AttachmentEntity::toModel).toList()
        );
    }
}
