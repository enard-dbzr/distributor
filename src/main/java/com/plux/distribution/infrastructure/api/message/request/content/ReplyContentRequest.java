package com.plux.distribution.infrastructure.api.message.request.content;

import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import jakarta.validation.constraints.NotNull;

public record ReplyContentRequest(
        @NotNull MessageContentRequest original,
        @NotNull Long replyTo
) implements MessageContentRequest {

    @Override
    public InteractionContent toModel() {
        return new ReplyMessageContent(
                original.toModel(),
                new InteractionId(replyTo)
        );
    }
}
