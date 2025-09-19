package com.plux.distribution.infrastructure.api.message.request.content;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.plux.distribution.core.message.domain.content.MessageContent;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(
        use = Id.NAME,
        include = As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleMessageContentRequest.class, name = "simple"),
        @JsonSubTypes.Type(value = ReplyContentRequest.class, name = "reply"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(value = "simple", schema = SimpleMessageContentRequest.class),
                @DiscriminatorMapping(value = "reply", schema = ReplyContentRequest.class),
        }
)
public sealed interface MessageContentRequest permits ReplyContentRequest, SimpleMessageContentRequest {

    MessageContent toModel();
}
