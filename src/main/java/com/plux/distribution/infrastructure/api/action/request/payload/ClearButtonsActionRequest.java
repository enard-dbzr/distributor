package com.plux.distribution.infrastructure.api.action.request.payload;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import jakarta.validation.constraints.NotNull;

public record ClearButtonsActionRequest(
        @NotNull Long messageId
) implements ActionPayloadRequest {

    @Override
    public ChatAction toModel(ChatId chatId) {
        return new ClearButtonsAction(chatId, new InteractionId(messageId));
    }
}
