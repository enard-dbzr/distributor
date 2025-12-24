package com.plux.distribution.core.integration.application.port.in;

import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FindInteractionSourceUseCase {

    @Nullable ServiceId findServiceId(@NotNull InteractionId interactionId);
}
