package com.plux.distribution.core.interaction.application.port.in;

import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public interface InteractionDeliveryUseCase {

    @NotNull InteractionId send(@NotNull DeliverInteractionCommand command);
}
