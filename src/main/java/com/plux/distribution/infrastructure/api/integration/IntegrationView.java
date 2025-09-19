package com.plux.distribution.infrastructure.api.integration;

import com.plux.distribution.core.integration.application.dto.Integration;
import jakarta.validation.constraints.NotNull;

public record IntegrationView(
        @NotNull Long id,
        @NotNull String webhook
) {

    public static IntegrationView of(Integration integration) {
        return new IntegrationView(integration.id().value(), integration.webhook().url());
    }
}
