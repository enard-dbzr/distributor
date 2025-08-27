package com.plux.distribution.application.port.in.integration;

import com.plux.distribution.application.dto.integration.ServiceWebhook;
import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface GetWebhookUseCase {
    @NotNull ServiceWebhook getWebhook(@NotNull ServiceId serviceId);
}
