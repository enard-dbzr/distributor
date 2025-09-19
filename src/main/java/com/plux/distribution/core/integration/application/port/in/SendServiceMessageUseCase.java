package com.plux.distribution.core.integration.application.port.in;

import com.plux.distribution.core.integration.application.command.SendServiceMessageCommand;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.message.domain.MessageId;
import org.jetbrains.annotations.NotNull;

public interface SendServiceMessageUseCase {

    @NotNull MessageId send(@NotNull SendServiceMessageCommand command) throws InvalidToken;
}
