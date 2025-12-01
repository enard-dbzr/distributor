package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.application.command.SendServiceMessageCommand;
import com.plux.distribution.core.integration.application.dto.ServiceToken;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.integration.application.port.in.SendServiceMessageUseCase;
import com.plux.distribution.core.integration.application.port.out.IntegrationRepositoryPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ServiceParticipant;
import org.jetbrains.annotations.NotNull;

public class SendServiceMessageService implements SendServiceMessageUseCase {

    private final InteractionDeliveryUseCase interactionDeliveryUseCase;
    private final IntegrationRepositoryPort integrationRepository;

    public SendServiceMessageService(InteractionDeliveryUseCase interactionDeliveryUseCase,
            IntegrationRepositoryPort integrationRepository) {
        this.interactionDeliveryUseCase = interactionDeliveryUseCase;
        this.integrationRepository = integrationRepository;
    }

    @Override
    public @NotNull InteractionId send(@NotNull SendServiceMessageCommand command) throws InvalidToken {
        Participant sender = new ServiceParticipant(findId(command.serviceToken()));
        Participant recipient = new ChatParticipant(new ChatId(command.chatId()));

        return interactionDeliveryUseCase.send(new DeliverInteractionCommand(
                sender,
                recipient,
                command.content()
        ));
    }

    private @NotNull ServiceId findId(@NotNull String token) throws InvalidToken {
        return integrationRepository.findId(new ServiceToken(token));
    }
}
