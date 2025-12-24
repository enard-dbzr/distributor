package com.plux.distribution.core.feedback.application.port.out;

import com.plux.distribution.core.feedback.domain.Feedback;
import org.jetbrains.annotations.NotNull;

public interface FeedbackProcessor {

    void process(@NotNull Feedback feedback);
}
