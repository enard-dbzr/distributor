package com.plux.distribution.core.message.application.port.out;

import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.participant.Participant;
import org.jetbrains.annotations.NotNull;

public interface MessageSenderPort {

    void send(@NotNull MessageId messageId, @NotNull Participant recipient,
            @NotNull MessageContent messageContent);
}
