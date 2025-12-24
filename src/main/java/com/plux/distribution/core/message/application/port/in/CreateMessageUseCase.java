package com.plux.distribution.core.message.application.port.in;

import com.plux.distribution.core.message.application.command.CreateMessageCommand;
import com.plux.distribution.core.message.application.dto.MessageDto;
import org.jetbrains.annotations.NotNull;

public interface CreateMessageUseCase {
    @NotNull MessageDto create(@NotNull CreateMessageCommand command);
}
