package com.plux.distribution.application.port.in.feedback;

import com.plux.distribution.application.dto.feedback.ButtonContext;
import com.plux.distribution.application.dto.feedback.MessageContext;
import org.jetbrains.annotations.NotNull;

public interface RegisterFeedbackUseCase {

    void handle_message(@NotNull MessageContext context);

    void handle_button(@NotNull ButtonContext context);
}
