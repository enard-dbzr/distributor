package com.plux.distribution.core.message.application.port.out;

import com.plux.distribution.core.message.domain.MessageModel;
import org.jetbrains.annotations.NotNull;

public interface UpdateMessagePort {
    void update(@NotNull MessageModel message);
}
