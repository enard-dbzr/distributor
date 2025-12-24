package com.plux.distribution.core.interaction.application.command;

import com.plux.distribution.core.interaction.domain.InteractionState.Pending;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import org.jetbrains.annotations.NotNull;

public record DeliverInteractionCommand(
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull InteractionContent content
) {

    public CreateInteractionCommand toCreateCommand() {
        return new CreateInteractionCommand(sender, recipient, new Pending(), content);
    }
}
