package com.plux.distribution.infrastructure.persistence.entity.interaction.attachment;

import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment.DisplayType;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.UUID;

@Entity
@DiscriminatorValue("MEDIA")
public class MediaAttachmentEntity extends AttachmentEntity {

    private UUID mediaId;

    @Enumerated(EnumType.STRING)
    private DisplayType displayType;  // TODO: может быть лучше добавить еще один DisplayType

    public MediaAttachmentEntity(UUID mediaId, DisplayType displayType) {
        this.mediaId = mediaId;
        this.displayType = displayType;
    }

    public MediaAttachmentEntity() {

    }

    @Override
    public MessageAttachment toModel() {
        return new MediaAttachment(new MediaId(mediaId), displayType);
    }
}
