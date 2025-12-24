package com.plux.distribution.core.feedback.application.port.out;

import com.plux.distribution.core.feedback.domain.ResolvedFeedback;
import org.jetbrains.annotations.NotNull;

public interface ResolvedFeedbackProcessor {

    void process(@NotNull ResolvedFeedback resolvedFeedback);
}
