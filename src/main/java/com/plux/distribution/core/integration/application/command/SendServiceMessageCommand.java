package com.plux.distribution.core.integration.application.command;

import com.plux.distribution.core.message.domain.content.MessageContent;
import org.jetbrains.annotations.NotNull;

public record SendServiceMessageCommand(
        @NotNull String serviceToken,
        @NotNull Long chatId,
        @NotNull MessageContent content
) {

}
