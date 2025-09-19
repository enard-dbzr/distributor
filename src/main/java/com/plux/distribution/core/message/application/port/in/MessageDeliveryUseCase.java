package com.plux.distribution.core.message.application.port.in;

import com.plux.distribution.core.message.application.command.SendMessageCommand;
import com.plux.distribution.core.message.domain.MessageId;
import org.jetbrains.annotations.NotNull;

public interface MessageDeliveryUseCase {
    @NotNull MessageId send(@NotNull SendMessageCommand command);
}
