package com.plux.distribution.infrastructure.api.message.request.attachment;

import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ButtonAttachmentRequest(
        @NotNull @NotBlank String text,
        @NotNull @NotBlank String tag
) implements MessageAttachmentRequest {

    @Override
    public MessageAttachment toModel() {
        return new ButtonAttachment(text, tag);
    }
}
