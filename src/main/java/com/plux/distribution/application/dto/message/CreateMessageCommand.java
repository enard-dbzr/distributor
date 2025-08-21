package com.plux.distribution.application.dto.message;

import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.state.MessageState;
import org.jetbrains.annotations.NotNull;

public record CreateMessageCommand(
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull MessageState state,
        @NotNull MessageContent content
) {
}
