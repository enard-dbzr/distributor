package com.plux.distribution.infrastructure.api.action.request.payload;

import com.plux.distribution.domain.action.ChatAction;
import com.plux.distribution.domain.action.ClearButtonsAction;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.message.MessageId;
import jakarta.validation.constraints.NotNull;

public record ClearButtonsActionRequest(
        @NotNull Long messageId
) implements ActionPayloadRequest {

    @Override
    public ChatAction toModel(ChatId chatId) {
        return new ClearButtonsAction(chatId, new MessageId(messageId));
    }
}
