package com.plux.distribution.application.port.in;

import com.plux.distribution.application.port.in.dto.ButtonData;
import com.plux.distribution.application.port.in.dto.MessageData;
import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface RegisterFeedbackUseCase {

    @NotNull MessageId handle_message(@NotNull MessageData messageData);

    void handle_button(@NotNull ButtonData buttonData);
}
