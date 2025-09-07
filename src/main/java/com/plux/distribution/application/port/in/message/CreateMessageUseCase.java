package com.plux.distribution.application.port.in.message;

import com.plux.distribution.application.dto.message.CreateMessageCommand;
import com.plux.distribution.application.dto.message.MessageDto;
import org.jetbrains.annotations.NotNull;

public interface CreateMessageUseCase {
    @NotNull MessageDto create(@NotNull CreateMessageCommand command);
}
