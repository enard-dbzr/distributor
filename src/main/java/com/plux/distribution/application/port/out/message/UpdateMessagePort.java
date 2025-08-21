package com.plux.distribution.application.port.out.message;

import com.plux.distribution.domain.message.Message;
import org.jetbrains.annotations.NotNull;

public interface UpdateMessagePort {
    void update(@NotNull Message message);
}
