package com.plux.distribution.core.integration.application.command;

import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import org.jetbrains.annotations.NotNull;

public record SendServiceMessageCommand(
        @NotNull String serviceToken,
        @NotNull Long chatId,
        @NotNull InteractionContent content
) {

}
