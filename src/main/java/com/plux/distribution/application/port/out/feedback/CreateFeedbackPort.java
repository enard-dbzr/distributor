package com.plux.distribution.application.port.out.feedback;

import com.plux.distribution.application.dto.feedback.CreateFeedbackCommand;
import com.plux.distribution.domain.feedback.Feedback;
import org.jetbrains.annotations.NotNull;

public interface CreateFeedbackPort {
    @NotNull Feedback create(@NotNull CreateFeedbackCommand command);
}
