package com.plux.distribution.core.interaction.application.port.out;

import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.domain.Interaction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InteractionRepositoryPort {

    @NotNull Interaction create(@NotNull CreateInteractionCommand command);

    @NotNull Interaction get(@NotNull InteractionId interactionId);

    @Nullable Interaction getLastOfRecipient(@NotNull Participant recipient);

    void update(@NotNull Interaction message);
}
