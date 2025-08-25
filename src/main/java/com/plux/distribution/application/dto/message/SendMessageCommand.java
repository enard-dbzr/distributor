package com.plux.distribution.application.dto.message;

import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.state.PendingState;
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
