package com.plux.distribution.application.port.out.integration;

import com.plux.distribution.application.dto.integration.CreateIntegrationCommand;
import com.plux.distribution.application.dto.integration.ServiceToken;
import com.plux.distribution.application.port.exception.InvalidToken;
import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface IntegrationRepositoryPort {
    @NotNull ServiceId create(@NotNull ServiceToken serviceToken, @NotNull CreateIntegrationCommand command);
    @NotNull ServiceId findId(@NotNull ServiceToken token) throws InvalidToken;
    @NotNull String getWebhook(@NotNull ServiceId id);
}
