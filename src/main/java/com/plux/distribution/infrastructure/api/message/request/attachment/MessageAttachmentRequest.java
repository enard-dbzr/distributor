package com.plux.distribution.infrastructure.api.message.request.attachment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;


@JsonTypeInfo(
        use = Id.NAME,
        include = As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @Type(value = ButtonAttachmentRequest.class, name = "button"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(value = "button", schema = ButtonAttachmentRequest.class),
        }
)
public sealed interface MessageAttachmentRequest permits ButtonAttachmentRequest {

    MessageAttachment toModel();
}
