package com.plux.distribution.application.port.out.specific.telegram.message;

import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface GetTgMessageIdPort {
    TgMessageGlobalId getTgMessageId(@NotNull MessageId messageId);
}
