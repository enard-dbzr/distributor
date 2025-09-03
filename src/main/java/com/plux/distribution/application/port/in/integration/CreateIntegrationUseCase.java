package com.plux.distribution.application.port.in.integration;

import com.plux.distribution.application.dto.integration.CreateIntegrationCommand;
import com.plux.distribution.application.dto.integration.ServiceToken;
import org.jetbrains.annotations.NotNull;

public interface CreateIntegrationUseCase {
    @NotNull ServiceToken create(@NotNull CreateIntegrationCommand command);
}
