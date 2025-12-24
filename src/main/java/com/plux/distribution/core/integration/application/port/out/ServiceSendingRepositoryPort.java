package com.plux.distribution.core.integration.application.port.out;

import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public interface ServiceSendingRepositoryPort {

    void save(@NotNull InteractionId sending, @NotNull ServiceId serviceId);

    @NotNull ServiceId getBySending(@NotNull InteractionId interactionId);
}
