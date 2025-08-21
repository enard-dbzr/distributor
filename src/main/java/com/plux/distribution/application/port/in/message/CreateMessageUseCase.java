package com.plux.distribution.application.port.in.message;

import com.plux.distribution.application.dto.message.CreateMessageCommand;
import com.plux.distribution.domain.message.MessageId;

public interface CreateMessageUseCase {
    MessageId create(CreateMessageCommand command);
}
