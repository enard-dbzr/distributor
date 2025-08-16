package com.plux.distribution.application.port.in;

import com.plux.distribution.application.port.in.context.ButtonContext;
import com.plux.distribution.application.port.in.context.MessageContext;
import org.jetbrains.annotations.NotNull;

public interface RegisterFeedbackUseCase {

    void handle_message(@NotNull MessageContext context);

    void handle_button(@NotNull ButtonContext context);
}
