package com.plux.distribution.infrastructure.api.action.request;

import com.plux.distribution.domain.action.ChatAction;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.infrastructure.api.action.request.payload.ActionPayloadRequest;
import jakarta.validation.constraints.NotNull;

public record ExecuteChatActionRequest(
        @NotNull Long chatId,
        @NotNull ActionPayloadRequest payload
) {
    public ChatAction toModel() {
        return payload.toModel(new ChatId(chatId));
    }
}
