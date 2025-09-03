package com.plux.distribution.application.service;

import com.plux.distribution.application.dto.integration.CreateIntegrationCommand;
import com.plux.distribution.application.dto.integration.SendServiceMessageCommand;
import com.plux.distribution.application.dto.integration.ServiceToken;
import com.plux.distribution.application.dto.integration.ServiceWebhook;
import com.plux.distribution.application.dto.message.SendMessageCommand;
import com.plux.distribution.application.port.exception.InvalidToken;
import com.plux.distribution.application.port.in.integration.CreateIntegrationUseCase;
import com.plux.distribution.application.port.in.integration.GetWebhookUseCase;
import com.plux.distribution.application.port.in.integration.SendServiceMessageUseCase;
import com.plux.distribution.application.port.in.message.MessageDeliveryUseCase;
import com.plux.distribution.application.port.out.integration.IntegrationRepositoryPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.service.ServiceId;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class IntegrationService implements GetWebhookUseCase, CreateIntegrationUseCase, SendServiceMessageUseCase {
    private final MessageDeliveryUseCase messageDeliveryUseCase;
    private final IntegrationRepositoryPort integrationRepository;

    public IntegrationService(MessageDeliveryUseCase messageDeliveryUseCase,
            IntegrationRepositoryPort integrationRepository) {
        this.messageDeliveryUseCase = messageDeliveryUseCase;
        this.integrationRepository = integrationRepository;
    }

    @Override
    public @NotNull ServiceWebhook getWebhook(@NotNull ServiceId serviceId) {
        return new ServiceWebhook(integrationRepository.getWebhook(serviceId));
    }

    @Override
    public @NotNull MessageId send(@NotNull SendServiceMessageCommand command) throws InvalidToken {
        Participant sender = new ServiceParticipant(findId(command.serviceToken()));
        Participant recipient = new ChatParticipant(new ChatId(command.chatId()));

        return messageDeliveryUseCase.send(new SendMessageCommand(
                sender,
                recipient,
                command.content()
        ));
    }

    @Override
    public @NotNull ServiceToken create(@NotNull CreateIntegrationCommand command) {
        var token = UUID.randomUUID().toString();

        integrationRepository.create(new ServiceToken(token), command);

        return new ServiceToken(token);
    }

    private @NotNull ServiceId findId(@NotNull String token) throws InvalidToken {
        return integrationRepository.findId(new ServiceToken(token));
    }
}
