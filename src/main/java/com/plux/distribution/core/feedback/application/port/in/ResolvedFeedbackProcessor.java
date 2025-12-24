package com.plux.distribution.core.feedback.application.port.in;

import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import org.jetbrains.annotations.NotNull;

public interface ResolvedFeedbackProcessor {

    void process(@NotNull ResolvedFeedback resolvedFeedback);
}
