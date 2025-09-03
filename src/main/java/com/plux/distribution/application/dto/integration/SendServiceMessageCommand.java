package com.plux.distribution.application.dto.integration;

import com.plux.distribution.domain.message.content.MessageContent;
import org.jetbrains.annotations.NotNull;

public record SendServiceMessageCommand(
        @NotNull String serviceToken,
        @NotNull Long chatId,
        @NotNull MessageContent content
) {

}
