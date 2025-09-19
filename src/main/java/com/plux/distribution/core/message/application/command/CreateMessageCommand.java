package com.plux.distribution.core.message.application.command;

import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.state.MessageState;
import org.jetbrains.annotations.NotNull;

public record CreateMessageCommand(
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull MessageState state,
        @NotNull MessageContent content
) {
}
