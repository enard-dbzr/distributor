package com.plux.distribution.application.port.out.message;

import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface UpdateMessagePort {
    void update(@NotNull MessageId id, @NotNull Message message);
}
