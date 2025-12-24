package com.plux.distribution.infrastructure.persistence.entity.interaction.content;

import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.persistence.entity.interaction.attachment.AttachmentEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("SIMPLE")
public class SimpleContentEntity extends InteractionContentEntity {

    @Column(columnDefinition = "TEXT")
    private String text;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "content_id")
    private List<AttachmentEntity> attachments = new ArrayList<>();

    public SimpleContentEntity(String text, List<AttachmentEntity> attachments) {
        this.text = text;
        this.attachments = attachments;
    }

    public SimpleContentEntity() {

    }

    @Override
    public InteractionContent toModel() {
        return new SimpleMessageContent(
                text,
                attachments.stream().map(AttachmentEntity::toModel).toList()
        );
    }
}
