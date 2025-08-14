package com.plux.distribution.application.port.in;

import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface MessageDeliveryUseCase {
    @NotNull MessageId send(@NotNull Message message);
}
