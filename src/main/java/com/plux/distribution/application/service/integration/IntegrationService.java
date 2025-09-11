package com.plux.distribution.application.service.integration;

import com.plux.distribution.application.dto.integration.IntegrationCommand;
import com.plux.distribution.application.dto.integration.CreateIntegrationResult;
import com.plux.distribution.application.dto.integration.Integration;
import com.plux.distribution.application.dto.integration.ServiceToken;
import com.plux.distribution.application.dto.integration.ServiceWebhook;
import com.plux.distribution.application.port.in.integration.CrudIntegrationUseCase;
import com.plux.distribution.application.port.in.integration.GetWebhookUseCase;
import com.plux.distribution.application.port.out.integration.IntegrationRepositoryPort;
import com.plux.distribution.domain.service.ServiceId;
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
