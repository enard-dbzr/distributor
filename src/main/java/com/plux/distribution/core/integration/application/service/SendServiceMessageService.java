package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.integration.application.command.SendServiceMessageCommand;
import com.plux.distribution.core.integration.application.dto.ServiceToken;
import com.plux.distribution.core.message.application.command.SendMessageCommand;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.integration.application.port.in.SendServiceMessageUseCase;
import com.plux.distribution.core.message.application.port.in.MessageDeliveryUseCase;
import com.plux.distribution.core.integration.application.port.out.IntegrationRepositoryPort;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.participant.ChatParticipant;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.participant.ServiceParticipant;
import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public class SendServiceMessageService implements SendServiceMessageUseCase {

    private final MessageDeliveryUseCase messageDeliveryUseCase;
    private final IntegrationRepositoryPort integrationRepository;

    public SendServiceMessageService(MessageDeliveryUseCase messageDeliveryUseCase,
            IntegrationRepositoryPort integrationRepository) {
        this.messageDeliveryUseCase = messageDeliveryUseCase;
        this.integrationRepository = integrationRepository;
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

    private @NotNull ServiceId findId(@NotNull String token) throws InvalidToken {
        return integrationRepository.findId(new ServiceToken(token));
    }
}
