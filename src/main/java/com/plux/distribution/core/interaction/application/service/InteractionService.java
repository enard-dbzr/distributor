package com.plux.distribution.core.interaction.application.service;

import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.application.port.in.CreateInteractionUseCase;
import com.plux.distribution.core.interaction.application.port.in.GetInteractionUseCase;
import com.plux.distribution.core.interaction.application.port.out.InteractionRepositoryPort;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class InteractionService implements CreateInteractionUseCase, GetInteractionUseCase {

    private final @NotNull InteractionRepositoryPort interactionRepository;

    public InteractionService(@NotNull InteractionRepositoryPort interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Override
    public @NotNull InteractionDto create(@NotNull CreateInteractionCommand command) {
        var message = interactionRepository.create(command);
        return new InteractionDto(message);
    }

    @Override
    public @NotNull InteractionDto get(@NotNull InteractionId interactionId) {
        return new InteractionDto(interactionRepository.get(interactionId));
    }

    @Override
    public Optional<InteractionDto> getLastOfRecipient(@NotNull Participant recipient) {
        var interaction = interactionRepository.getLastOfRecipient(recipient);
        if (interaction == null) {
            return Optional.empty();
        }

        return Optional.of(new InteractionDto(interaction));
    }
}
