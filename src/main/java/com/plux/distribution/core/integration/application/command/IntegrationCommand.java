package com.plux.distribution.core.integration.application.command;

import org.jetbrains.annotations.NotNull;

public record IntegrationCommand(
        @NotNull String webhook
) {

}
