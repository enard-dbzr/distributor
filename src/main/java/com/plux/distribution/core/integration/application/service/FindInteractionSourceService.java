package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.integration.application.port.in.FindInteractionSourceUseCase;
import com.plux.distribution.core.integration.application.port.out.ServiceSendingRepositoryPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FindInteractionSourceService implements FindInteractionSourceUseCase {

    private final @NotNull ServiceSendingRepositoryPort serviceSendingRepositoryPort;

    public FindInteractionSourceService(@NotNull ServiceSendingRepositoryPort serviceSendingRepositoryPort) {
        this.serviceSendingRepositoryPort = serviceSendingRepositoryPort;
    }

    @Override
    public @Nullable ServiceId findServiceId(@NotNull InteractionId interactionId) {
        return serviceSendingRepositoryPort.getBySending(interactionId);
    }
}
