package com.plux.distribution.application.port.out;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.participant.Participant;
import org.jetbrains.annotations.NotNull;

public interface MessageSenderPort {

    void send(@NotNull MessageId messageId, @NotNull Participant recipient,
            @NotNull MessageContent messageContent);
}
