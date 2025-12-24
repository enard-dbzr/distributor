package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.integration.application.command.IntegrationCommand;
import com.plux.distribution.core.integration.application.command.CreateIntegrationResult;
import com.plux.distribution.core.integration.application.dto.Integration;
import com.plux.distribution.core.integration.application.dto.ServiceToken;
import com.plux.distribution.core.integration.application.dto.ServiceWebhook;
import com.plux.distribution.core.integration.application.port.in.CrudIntegrationUseCase;
import com.plux.distribution.core.integration.application.port.in.GetWebhookUseCase;
import com.plux.distribution.core.integration.application.port.out.IntegrationRepositoryPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class IntegrationService implements GetWebhookUseCase, CrudIntegrationUseCase {

    private final IntegrationRepositoryPort integrationRepository;

    public IntegrationService(IntegrationRepositoryPort integrationRepository) {
        this.integrationRepository = integrationRepository;
    }

    @Override
    public @NotNull ServiceWebhook getWebhook(@NotNull ServiceId serviceId) {
        return new ServiceWebhook(integrationRepository.getWebhook(serviceId));
    }

    @Override
    public @NotNull CreateIntegrationResult create(@NotNull IntegrationCommand command) {
        var token = new ServiceToken(UUID.randomUUID().toString());

        var serviceId = integrationRepository.create(token, command);

        return new CreateIntegrationResult(token, serviceId);
    }

    @Override
    public List<Integration> getAll() {
        return integrationRepository.getAll();
    }

    @Override
    public void delete(@NotNull ServiceId id) {
        integrationRepository.delete(id);
    }

    @Override
    public void put(@NotNull ServiceId id, @NotNull IntegrationCommand command) {
        integrationRepository.put(id, command);
    }
}
