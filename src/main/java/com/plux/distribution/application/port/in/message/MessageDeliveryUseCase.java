package com.plux.distribution.application.port.in.message;

import com.plux.distribution.application.dto.message.SendMessageCommand;
import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface MessageDeliveryUseCase {
    @NotNull MessageId send(@NotNull SendMessageCommand command);
}
