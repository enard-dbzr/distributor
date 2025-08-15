package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.dto.ButtonData;
import com.plux.distribution.application.port.in.dto.MessageData;
import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public class RegisterFeedbackService implements RegisterFeedbackUseCase {

    @Override
    public @NotNull MessageId handle_message(@NotNull MessageData messageData) {
        System.out.println(messageData);
        return new MessageId(1L);
    }

    @Override
    public void handle_button(@NotNull ButtonData buttonData) {
        System.out.println(buttonData);
    }
}
