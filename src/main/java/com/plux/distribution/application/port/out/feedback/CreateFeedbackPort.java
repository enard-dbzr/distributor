package com.plux.distribution.application.port.out.feedback;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.feedback.FeedbackId;
import org.jetbrains.annotations.NotNull;

public interface CreateFeedbackPort {
    @NotNull FeedbackId create(@NotNull Feedback feedback);
}
