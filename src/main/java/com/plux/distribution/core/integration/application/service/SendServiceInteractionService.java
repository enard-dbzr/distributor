package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.application.command.SendServiceInteractionCommand;
import com.plux.distribution.core.integration.application.dto.ServiceToken;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.integration.application.port.in.SendServiceInteractionUseCase;
import com.plux.distribution.core.integration.application.port.out.IntegrationRepositoryPort;
import com.plux.distribution.core.integration.application.port.out.ServiceSendingRepositoryPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import org.jetbrains.annotations.NotNull;

public class SendServiceInteractionService implements SendServiceInteractionUseCase {

    private final @NotNull InteractionDeliveryUseCase interactionDeliveryUseCase;
    private final @NotNull IntegrationRepositoryPort integrationRepository;
    private final @NotNull ServiceSendingRepositoryPort serviceSendingRepository;


    public SendServiceInteractionService(@NotNull InteractionDeliveryUseCase interactionDeliveryUseCase,
            @NotNull IntegrationRepositoryPort integrationRepository,
            @NotNull ServiceSendingRepositoryPort serviceSendingRepository) {
        this.interactionDeliveryUseCase = interactionDeliveryUseCase;
        this.integrationRepository = integrationRepository;
        this.serviceSendingRepository = serviceSendingRepository;
    }

    @Override
    public @NotNull InteractionId send(@NotNull SendServiceInteractionCommand command) throws InvalidToken {
        var serviceId = findId(command.serviceToken());

        var interactionId = interactionDeliveryUseCase.deliver(new DeliverInteractionCommand(
                new BotParticipant(),
                new ChatParticipant(new ChatId(command.chatId())),
                command.content()
        ));

        serviceSendingRepository.save(interactionId, serviceId);

        return interactionId;
    }

    private @NotNull ServiceId findId(@NotNull String token) throws InvalidToken {
        return integrationRepository.findId(new ServiceToken(token));
    }
}
