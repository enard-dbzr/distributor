package com.plux.distribution.infrastructure.api.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.attachment.MessageAttachment;
import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

record SendMessageRequest(
        @NotNull Long chatId,
        @NotNull @Valid MessageContentRequest content
) {

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SimpleMessageContentRequest.class, name = "simple")
    })
    sealed interface MessageContentRequest permits SimpleMessageContentRequest {

        @NotNull MessageContent toModel();
    }

    private record SimpleMessageContentRequest(
            @NotNull String text,
            @NotNull @Valid List<@NotNull ButtonAttachmentRequest> attachments
    )
            implements MessageContentRequest {

        @Override
        public @NotNull MessageContent toModel() {
            return new SimpleMessageContent(
                    text,
                    attachments.stream()
                            .map(MessageAttachmentRequest::toModel)
                            .toList()
            );
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ButtonAttachmentRequest.class, name = "button")
    })
    private sealed interface MessageAttachmentRequest permits ButtonAttachmentRequest {

        @NotNull MessageAttachment toModel();
    }

    private record ButtonAttachmentRequest(
            @NotBlank String text,
            @NotBlank String tag
    ) implements MessageAttachmentRequest {

        @Override
        public @NotNull MessageAttachment toModel() {
            return new ButtonAttachment(text, tag);
        }
    }
}
