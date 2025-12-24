package com.plux.distribution.core.interaction.application.service;

import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.application.port.out.BotInteractionHandler;
import com.plux.distribution.core.interaction.application.port.out.InteractionRepositoryPort;
import com.plux.distribution.core.interaction.application.port.out.InteractionSenderPort;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class InteractionDeliveryService implements InteractionDeliveryUseCase {

    private final @NotNull InteractionRepositoryPort interactionRepository;

    private final @NotNull InteractionSenderPort interactionSenderPort;
    private BotInteractionHandler botInteractionHandler;

    public InteractionDeliveryService(@NotNull InteractionRepositoryPort interactionRepository,
            @NotNull InteractionSenderPort interactionSenderPort) {
        this.interactionRepository = interactionRepository;
        this.interactionSenderPort = interactionSenderPort;
    }

    @Override
    public @NotNull InteractionId deliver(@NotNull DeliverInteractionCommand command) {
        var interaction = interactionRepository.create(command.toCreateCommand());

        switch (interaction.getRecipient()) {
            case ChatParticipant _ -> {
                interactionSenderPort.send(interaction.getId(), interaction.getRecipient(), interaction.getContent());
                interaction.markTransferred(new Date());
                interactionRepository.update(interaction);
            }
            case BotParticipant _ -> {
                interaction.markTransferred(new Date());
                interactionRepository.update(interaction);
                botInteractionHandler.handle(new InteractionDto(interaction));
            }
            default -> throw new UnsupportedOperationException();
        }

        return interaction.getId();
    }

    public void setBotInteractionHandler(@NotNull BotInteractionHandler botInteractionHandler) {
        this.botInteractionHandler = botInteractionHandler;
    }
}
