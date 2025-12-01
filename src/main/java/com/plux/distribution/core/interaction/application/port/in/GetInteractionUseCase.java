package com.plux.distribution.core.interaction.application.port.in;

import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface GetInteractionUseCase {

    @NotNull InteractionDto get(@NotNull InteractionId interactionId);

    Optional<InteractionDto> getLastOfRecipient(@NotNull Participant recipient);
}
