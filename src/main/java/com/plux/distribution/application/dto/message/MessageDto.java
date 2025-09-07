package com.plux.distribution.application.dto.message;

import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.state.MessageState;
import org.jetbrains.annotations.NotNull;

public record MessageDto(
        @NotNull MessageId id,
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull MessageState state,
        @NotNull MessageContent content
) {

    public MessageDto(Message model) {
        this(model.getId(), model.getSender(), model.getRecipient(), model.getState(), model.getContent());
    }
}
