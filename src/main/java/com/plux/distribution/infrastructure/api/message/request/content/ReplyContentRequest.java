package com.plux.distribution.infrastructure.api.message.request.content;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.content.ReplyMessageContent;
import jakarta.validation.constraints.NotNull;

public record ReplyContentRequest(
        @NotNull MessageContentRequest original,
        @NotNull Long replyTo
) implements MessageContentRequest {

    @Override
    public MessageContent toModel() {
        return new ReplyMessageContent(
                original.toModel(),
                new MessageId(replyTo)
        );
    }
}
