package com.plux.distribution.application.service.integration;

import com.plux.distribution.application.dto.integration.CreateIntegrationCommand;
import com.plux.distribution.application.dto.integration.ServiceToken;
import com.plux.distribution.application.dto.integration.ServiceWebhook;
import com.plux.distribution.application.port.in.integration.CreateIntegrationUseCase;
import com.plux.distribution.application.port.in.integration.GetWebhookUseCase;
import com.plux.distribution.application.port.out.integration.IntegrationRepositoryPort;
import com.plux.distribution.domain.service.ServiceId;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class IntegrationService implements GetWebhookUseCase, CreateIntegrationUseCase {
    private final IntegrationRepositoryPort integrationRepository;

    public IntegrationService(IntegrationRepositoryPort integrationRepository) {
        this.integrationRepository = integrationRepository;
    }

    @Override
    public @NotNull ServiceWebhook getWebhook(@NotNull ServiceId serviceId) {
        return new ServiceWebhook(integrationRepository.getWebhook(serviceId));
    }

    @Override
    public @NotNull ServiceToken create(@NotNull CreateIntegrationCommand command) {
        var token = UUID.randomUUID().toString();

        integrationRepository.create(new ServiceToken(token), command);

        return new ServiceToken(token);
    }
}
