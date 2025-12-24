package com.plux.distribution.core.integration.application.port.in;

import com.plux.distribution.core.integration.application.command.CreateIntegrationResult;
import com.plux.distribution.core.integration.application.command.IntegrationCommand;
import com.plux.distribution.core.integration.application.dto.Integration;
import com.plux.distribution.core.integration.domain.ServiceId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface CrudIntegrationUseCase {

    @NotNull CreateIntegrationResult create(@NotNull IntegrationCommand command);

    List<Integration> getAll();

    void delete(@NotNull ServiceId id);

    void put(@NotNull ServiceId id, @NotNull IntegrationCommand command);
}
