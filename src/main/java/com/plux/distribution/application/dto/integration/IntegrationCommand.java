package com.plux.distribution.application.dto.integration;

import org.jetbrains.annotations.NotNull;

public record IntegrationCommand(
        @NotNull String webhook
) {

}
