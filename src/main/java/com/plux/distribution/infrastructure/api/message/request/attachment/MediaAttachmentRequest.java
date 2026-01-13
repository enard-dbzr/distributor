package com.plux.distribution.infrastructure.api.message.request.attachment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment.DisplayType;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MediaAttachmentRequest(
        @NotNull UUID mediaId,
        @NotNull DisplayTypeRequest displayType
) implements MessageAttachmentRequest {

    @Override
    public MessageAttachment toModel() {
        return new MediaAttachment(
                new MediaId(mediaId),
                displayType.toDomain()
        );
    }

    enum DisplayTypeRequest {
        @JsonProperty("photo") PHOTO,
        @JsonProperty("document") DOCUMENT;

        public DisplayType toDomain() {
            return switch (this) {
                case PHOTO -> DisplayType.PHOTO;
                case DOCUMENT -> DisplayType.DOCUMENT;
            };
        }
    }
}
