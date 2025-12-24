package com.plux.distribution.core.interaction.application.port.out;

import com.plux.distribution.core.interaction.application.dto.InteractionDto;

public interface BotInteractionHandler {

    void handle(InteractionDto interaction);
}
