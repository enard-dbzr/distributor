package com.plux.distribution.core.feedback.application.port.in.register;

import org.jetbrains.annotations.NotNull;

public interface RegisterFeedbackUseCase {

    void handle_message(@NotNull MessageContext context);

    void handle_button(@NotNull ButtonContext context);
}
