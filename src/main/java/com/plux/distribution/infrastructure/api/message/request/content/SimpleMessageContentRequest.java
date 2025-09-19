package com.plux.distribution.infrastructure.api.message.request.content;

import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.api.message.request.attachment.MessageAttachmentRequest;
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
