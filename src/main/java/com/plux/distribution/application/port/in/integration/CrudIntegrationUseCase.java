package com.plux.distribution.application.port.in.integration;

import com.plux.distribution.application.dto.integration.IntegrationCommand;
import com.plux.distribution.application.dto.integration.CreateIntegrationResult;
import com.plux.distribution.application.dto.integration.Integration;
import com.plux.distribution.domain.service.ServiceId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface CrudIntegrationUseCase {

    @NotNull CreateIntegrationResult create(@NotNull IntegrationCommand command);

    List<Integration> getAll();

    void delete(@NotNull ServiceId id);

    void put(@NotNull ServiceId id, @NotNull IntegrationCommand command);
}
