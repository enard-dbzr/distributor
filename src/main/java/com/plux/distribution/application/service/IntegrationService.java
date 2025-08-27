package com.plux.distribution.application.service;

import com.plux.distribution.application.dto.integration.ServiceWebhook;
import com.plux.distribution.application.port.in.integration.GetWebhookUseCase;
import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public class IntegrationService implements GetWebhookUseCase {
    private final String url;

    public IntegrationService(String url) {
        this.url = url;
    }

    @Override
    public @NotNull ServiceWebhook getWebhook(@NotNull ServiceId serviceId) {
        return new ServiceWebhook(url);
    }
}
