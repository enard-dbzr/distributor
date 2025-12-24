package com.plux.distribution.core.message.application.port.in;

import com.plux.distribution.core.message.application.dto.MessageDto;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.participant.Participant;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface GetMessageUseCase {

    @NotNull MessageDto getMessage(@NotNull MessageId messageId);

    Optional<MessageDto> getLastOfRecipient(@NotNull Participant recipient);


}
