package com.plux.distribution.core.interaction.application.dto;

import com.plux.distribution.core.interaction.domain.Interaction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.InteractionState;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import org.jetbrains.annotations.NotNull;

public record InteractionDto(
        @NotNull InteractionId id,
        @NotNull Participant sender,
        @NotNull Participant recipient,
        @NotNull InteractionContent content,
        @NotNull InteractionState state
) {

    public InteractionDto(Interaction model) {
        this(model.getId(), model.getSender(), model.getRecipient(), model.getContent(), model.getState());
    }
}
