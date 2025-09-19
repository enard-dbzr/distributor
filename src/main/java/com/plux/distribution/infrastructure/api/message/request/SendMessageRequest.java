package com.plux.distribution.infrastructure.api.message.request;

import com.plux.distribution.infrastructure.api.message.request.content.MessageContentRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record SendMessageRequest(
        @NotNull Long chatId,

        @NotNull @Valid MessageContentRequest content
) {

}
