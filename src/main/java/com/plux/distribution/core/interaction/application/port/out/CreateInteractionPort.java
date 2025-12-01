package com.plux.distribution.core.interaction.application.port.out;

import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.domain.Interaction;
import org.jetbrains.annotations.NotNull;

public interface CreateInteractionPort {

    @NotNull Interaction create(@NotNull CreateInteractionCommand command);
}
