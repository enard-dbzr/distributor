package com.plux.distribution.infrastructure.notifier.view.message;

import com.plux.distribution.application.dto.message.MessageDto;
import com.plux.distribution.infrastructure.notifier.view.message.content.MessageContentView;
import com.plux.distribution.infrastructure.notifier.view.message.participant.ParticipantView;
import com.plux.distribution.infrastructure.notifier.view.message.state.MessageStateView;
import org.jetbrains.annotations.NotNull;

public record MessageView(
        @NotNull Long id,
        @NotNull ParticipantView sender,
        @NotNull ParticipantView recipient,
        @NotNull MessageStateView state,
        @NotNull MessageContentView content
) {

    public MessageView(@NotNull MessageDto message) {
        this(
                message.id().value(),
                ParticipantView.create(message.sender()),
                ParticipantView.create(message.recipient()),
                MessageStateView.create(message.state()),
                MessageContentView.create(message.content())
        );
    }
}
