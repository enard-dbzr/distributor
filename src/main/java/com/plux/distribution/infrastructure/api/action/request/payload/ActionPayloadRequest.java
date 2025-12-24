package com.plux.distribution.infrastructure.api.action.request.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(
        use = Id.NAME,
        include = As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClearButtonsActionRequest.class, name = "clear_buttons"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = ClearButtonsActionRequest.class, value = "clear_buttons"),
        }
)
public sealed interface ActionPayloadRequest permits ClearButtonsActionRequest {

    ChatAction toModel(ChatId chatId);
}
