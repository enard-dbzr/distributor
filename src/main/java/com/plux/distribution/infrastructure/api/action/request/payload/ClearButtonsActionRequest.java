package com.plux.distribution.infrastructure.api.action.request.payload;

import com.plux.distribution.core.message.application.dto.action.ChatAction;
import com.plux.distribution.core.message.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.message.domain.MessageId;
import jakarta.validation.constraints.NotNull;

public record ClearButtonsActionRequest(
        @NotNull Long messageId
) implements ActionPayloadRequest {

    @Override
    public ChatAction toModel(ChatId chatId) {
        return new ClearButtonsAction(chatId, new MessageId(messageId));
    }
}
