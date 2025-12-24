package com.plux.distribution.infrastructure.telegram.port;

import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public interface TgMessageLinker {

    void link(@NotNull InteractionId internal, @NotNull TgMessageGlobalId external);

    @NotNull InteractionId getInteractionId(@NotNull TgMessageGlobalId messageGlobalId);

    TgMessageGlobalId getTgMessageId(@NotNull InteractionId interactionId);
}
