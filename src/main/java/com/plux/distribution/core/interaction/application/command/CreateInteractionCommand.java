package com.plux.distribution.core.interaction.application.command;

import com.plux.distribution.core.interaction.domain.InteractionState;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import org.jetbrains.annotations.NotNull;

public record CreateInteractionCommand(
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull InteractionState state,
        @NotNull InteractionContent content
) {
}
