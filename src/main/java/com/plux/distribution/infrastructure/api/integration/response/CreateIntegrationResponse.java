package com.plux.distribution.infrastructure.api.integration.response;

import com.plux.distribution.core.integration.application.command.CreateIntegrationResult;
import jakarta.validation.constraints.NotNull;

public record CreateIntegrationResponse(
        @NotNull String token,
        @NotNull Long id
) {
    public static CreateIntegrationResponse of(CreateIntegrationResult result) {
        return new CreateIntegrationResponse(result.token().token(), result.id().value());
    }
}
