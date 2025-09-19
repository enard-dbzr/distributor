package com.plux.distribution.core.message.application.port.out;

import com.plux.distribution.core.message.application.command.CreateMessageCommand;
import com.plux.distribution.core.message.domain.MessageModel;
import org.jetbrains.annotations.NotNull;

public interface CreateMessagePort {
    @NotNull MessageModel create(@NotNull CreateMessageCommand command);
}
