package com.plux.distribution.core.interaction.application.port.in;

import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import org.jetbrains.annotations.NotNull;

// TODO: remove this
public interface CreateInteractionUseCase {

    @NotNull InteractionDto create(@NotNull CreateInteractionCommand command);
}
