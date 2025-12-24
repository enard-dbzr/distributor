package com.plux.distribution.core.integration.application.command;

import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import org.jetbrains.annotations.NotNull;

public record SendServiceInteractionCommand(
        @NotNull String serviceToken,
        @NotNull Long chatId,
        @NotNull InteractionContent content
) {

}
