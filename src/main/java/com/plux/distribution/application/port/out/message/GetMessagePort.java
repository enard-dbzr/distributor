package com.plux.distribution.application.port.out.message;

import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.participant.Participant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GetMessagePort {

    @NotNull Message get(@NotNull MessageId messageId);

    @Nullable Message getLastOfRecipient(@NotNull Participant recipient);

}
