package com.plux.distribution.core.integration.application.port.out;

import com.plux.distribution.core.integration.application.command.IntegrationCommand;
import com.plux.distribution.core.integration.application.dto.Integration;
import com.plux.distribution.core.integration.application.dto.ServiceToken;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.integration.domain.ServiceId;
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
