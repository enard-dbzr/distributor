package com.plux.distribution.infrastructure.telegram.port.message;

import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public interface GetMessageIdByTgPort {

    @NotNull InteractionId getInteractionId(@NotNull TgMessageGlobalId messageGlobalId);
}
