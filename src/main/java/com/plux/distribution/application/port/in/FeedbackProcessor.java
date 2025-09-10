package com.plux.distribution.application.port.in;

import com.plux.distribution.application.dto.feedback.dto.Feedback;
import org.jetbrains.annotations.NotNull;

public interface FeedbackProcessor {
    void process(@NotNull Feedback feedback);
}
