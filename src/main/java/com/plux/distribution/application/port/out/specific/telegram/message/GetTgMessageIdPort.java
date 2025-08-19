package com.plux.distribution.application.port.out.specific.telegram.message;

import com.plux.distribution.domain.message.MessageId;

public interface GetTgMessageIdPort {
    Integer getTgMessageId(MessageId messageId);
}
