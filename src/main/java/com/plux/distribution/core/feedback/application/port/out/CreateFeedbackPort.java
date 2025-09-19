package com.plux.distribution.core.feedback.application.port.out;

import com.plux.distribution.core.feedback.application.command.CreateFeedbackCommand;
import com.plux.distribution.core.feedback.application.dto.Feedback;
import org.jetbrains.annotations.NotNull;

public interface CreateFeedbackPort {
    @NotNull Feedback create(@NotNull CreateFeedbackCommand command);
}
