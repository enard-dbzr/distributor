package com.plux.distribution.core.message.application.port.out;

import com.plux.distribution.core.message.domain.MessageModel;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.participant.Participant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GetMessagePort {

    @NotNull MessageModel get(@NotNull MessageId messageId);

    @Nullable MessageModel getLastOfRecipient(@NotNull Participant recipient);

}
