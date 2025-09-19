package com.plux.distribution.infrastructure.api.integration.request;

import com.plux.distribution.core.integration.application.command.IntegrationCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IntegrationRequest(@NotNull @NotBlank String webhook) {
    public IntegrationCommand toCommand() {
        return new IntegrationCommand(webhook);
    }
}
