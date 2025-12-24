package com.plux.distribution.core.interaction.application.dto.action;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.domain.InteractionId;

public class ClearButtonsAction extends ChatAction {

    private final InteractionId interactionId;

    public ClearButtonsAction(ChatId chatId, InteractionId interactionId) {
        super(chatId);
        this.interactionId = interactionId;
    }

    public InteractionId getMessageId() {
        return interactionId;
    }

    @Override
    public void accept(ChatActionVisitor visitor) {
        visitor.visit(this);
    }
}
