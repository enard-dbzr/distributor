package com.plux.distribution.infrastructure.notifier.view.interaction;

import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import org.jetbrains.annotations.NotNull;

public record InteractionView(
        @NotNull Long id,
        @NotNull ParticipantView sender,
        @NotNull ParticipantView recipient,
        @NotNull InteractionContentView content,
        @NotNull InteractionStateView state
) {

    public InteractionView(@NotNull InteractionDto interaction) {
        this(
                interaction.id().value(),
                ParticipantView.create(interaction.sender()),
                ParticipantView.create(interaction.recipient()),
                InteractionContentView.create(interaction.content()),
                InteractionStateView.create(interaction.state())
        );
    }
}
