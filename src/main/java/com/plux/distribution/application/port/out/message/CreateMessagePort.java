package com.plux.distribution.application.port.out.message;

import com.plux.distribution.application.dto.message.CreateMessageCommand;
import com.plux.distribution.domain.message.Message;
import org.jetbrains.annotations.NotNull;

public interface CreateMessagePort {
    @NotNull Message create(@NotNull CreateMessageCommand command);
}
