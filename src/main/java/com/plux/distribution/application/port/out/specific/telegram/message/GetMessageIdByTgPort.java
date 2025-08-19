package com.plux.distribution.application.port.out.specific.telegram.message;

import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface GetMessageIdByTgPort {

    @NotNull MessageId getMessageId(@NotNull TgMessageGlobalId messageGlobalId);
}
