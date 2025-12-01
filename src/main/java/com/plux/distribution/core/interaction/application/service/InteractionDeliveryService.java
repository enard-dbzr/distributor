package com.plux.distribution.core.interaction.application.service;

import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.application.port.in.CreateInteractionUseCase;
import com.plux.distribution.core.interaction.application.port.in.GetInteractionUseCase;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.application.port.out.CreateInteractionPort;
import com.plux.distribution.core.interaction.application.port.out.GetInteractionPort;
import com.plux.distribution.core.interaction.application.port.out.MessageSenderPort;
import com.plux.distribution.core.interaction.application.port.out.UpdateInteractionPort;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import java.util.Date;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class InteractionDeliveryService implements InteractionDeliveryUseCase, CreateInteractionUseCase,
        GetInteractionUseCase {

    private final MessageSenderPort messageSenderPort;
    private final CreateInteractionPort createInteractionPort;
    private final UpdateInteractionPort updateInteractionPort;
    private final GetInteractionPort getInteractionPort;

    public InteractionDeliveryService(MessageSenderPort messageSenderPort,
            CreateInteractionPort createInteractionPort, UpdateInteractionPort updateInteractionPort,
            GetInteractionPort getInteractionPort) {
        this.messageSenderPort = messageSenderPort;
        this.createInteractionPort = createInteractionPort;
        this.updateInteractionPort = updateInteractionPort;
        this.getInteractionPort = getInteractionPort;
    }


    @Override
    public @NotNull InteractionId send(@NotNull DeliverInteractionCommand command) {
        var message = createInteractionPort.create(command.toCreateCommand());

        messageSenderPort.send(message.getId(), command.recipient(), command.content());
        message.markSent(new Date());

        updateInteractionPort.update(message);

        return message.getId();
    }

    @Override
    public @NotNull InteractionDto create(@NotNull CreateInteractionCommand command) {
        var message = createInteractionPort.create(command);
        return new InteractionDto(message);
    }

    @Override
    public @NotNull InteractionDto get(@NotNull InteractionId interactionId) {
        return new InteractionDto(getInteractionPort.get(interactionId));
    }

    @Override
    public Optional<InteractionDto> getLastOfRecipient(@NotNull Participant recipient) {
        var message = getInteractionPort.getLastOfRecipient(recipient);
        if (message == null) {
            return Optional.empty();
        }

        return Optional.of(new InteractionDto(message));
    }
}
