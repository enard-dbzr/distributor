package com.plux.distribution.application.port.in.feedback;

import com.plux.distribution.application.dto.feedback.dto.ResolvedFeedback;
import org.jetbrains.annotations.NotNull;

public interface ResolvedFeedbackProcessor {

    void process(@NotNull ResolvedFeedback resolvedFeedback);
}
