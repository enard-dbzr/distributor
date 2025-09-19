package com.plux.distribution.core.message.application.command;

import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.state.PendingState;
import org.jetbrains.annotations.NotNull;

public record SendMessageCommand(
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull MessageContent content
) {
    public CreateMessageCommand toCreateCommand() {
        return new CreateMessageCommand(sender, recipient, new PendingState(), content);
    }
}
