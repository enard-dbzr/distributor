package com.plux.distribution.infrastructure.telegram.port.message;

import com.plux.distribution.core.message.domain.MessageId;
import org.jetbrains.annotations.NotNull;

public interface GetMessageIdByTgPort {

    @NotNull MessageId getMessageId(@NotNull TgMessageGlobalId messageGlobalId);
}
