package com.plux.distribution.application.port.in;

import com.plux.distribution.application.service.FeedbackContext;
import org.jetbrains.annotations.NotNull;

public interface FeedbackProcessor {
    void process(@NotNull FeedbackContext feedback);
}
