package com.plux.distribution.infrastructure.api.message.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.plux.distribution.domain.message.content.MessageContent;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(
        use = Id.NAME,
        include = As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleMessageContentRequest.class, name = "simple"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(value = "simple", schema = SimpleMessageContentRequest.class),
        }
)
public sealed interface MessageContentRequest permits SimpleMessageContentRequest {

    MessageContent toModel();
}
