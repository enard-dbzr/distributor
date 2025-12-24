package com.plux.distribution.core.feedback.application.port.in;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import org.jetbrains.annotations.NotNull;

public interface FeedbackProcessor {

    void process(@NotNull Feedback feedback);
}
