package com.plux.distribution.core.message.application.dto;

import com.plux.distribution.core.message.domain.MessageModel;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.state.MessageState;
import org.jetbrains.annotations.NotNull;

public record MessageDto(
        @NotNull MessageId id,
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull MessageState state,
        @NotNull MessageContent content
) {

    public MessageDto(MessageModel model) {
        this(model.getId(), model.getSender(), model.getRecipient(), model.getState(), model.getContent());
    }
}
