package com.plux.distribution.application.port.in.integration;

import com.plux.distribution.application.dto.integration.SendServiceMessageCommand;
import com.plux.distribution.application.port.exception.InvalidToken;
import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface SendServiceMessageUseCase {

    @NotNull MessageId send(@NotNull SendServiceMessageCommand command) throws InvalidToken;
}
