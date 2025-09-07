package com.plux.distribution.application.port.in.message;

import com.plux.distribution.application.dto.message.MessageDto;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.participant.Participant;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface GetMessageUseCase {

    @NotNull MessageDto getMessage(@NotNull MessageId messageId);

    Optional<MessageDto> getLastOfRecipient(@NotNull Participant recipient);


}
