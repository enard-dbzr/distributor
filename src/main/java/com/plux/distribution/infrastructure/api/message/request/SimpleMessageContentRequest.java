package com.plux.distribution.infrastructure.api.message.request;

import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SimpleMessageContentRequest(
        @NotNull String text,
        @NotNull List<MessageAttachmentRequest> attachments
) implements MessageContentRequest {


    @Override
    public MessageContent toModel() {
        return new SimpleMessageContent(
                text,
                attachments.stream()
                        .map(MessageAttachmentRequest::toModel)
                        .toList()
        );
    }
}
