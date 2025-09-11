package com.plux.distribution.application.port.out.integration;

import com.plux.distribution.application.dto.integration.IntegrationCommand;
import com.plux.distribution.application.dto.integration.Integration;
import com.plux.distribution.application.dto.integration.ServiceToken;
import com.plux.distribution.application.port.exception.InvalidToken;
import com.plux.distribution.domain.service.ServiceId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface IntegrationRepositoryPort {
    @NotNull ServiceId create(@NotNull ServiceToken serviceToken, @NotNull IntegrationCommand command);
    @NotNull ServiceId findId(@NotNull ServiceToken token) throws InvalidToken;
    @NotNull String getWebhook(@NotNull ServiceId id);
    @NotNull List<Integration> getAll();
    void delete(ServiceId id);
    void put(@NotNull ServiceId id, @NotNull IntegrationCommand command);
}
