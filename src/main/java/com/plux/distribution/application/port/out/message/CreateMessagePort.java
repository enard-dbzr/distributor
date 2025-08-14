package com.plux.distribution.application.port.out.message;

import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface CreateMessagePort {
    @NotNull MessageId create(@NotNull Message message);
}
