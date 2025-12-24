package com.plux.distribution.core.integration.application.port.in;

import com.plux.distribution.core.integration.application.command.SendServiceInteractionCommand;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public interface SendServiceInteractionUseCase {

    @NotNull InteractionId send(@NotNull SendServiceInteractionCommand command) throws InvalidToken;
}
