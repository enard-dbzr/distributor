package com.plux.distribution.infrastructure.notifier.view.feedback;

import com.plux.distribution.application.dto.feedback.dto.ResolvedFeedback;
import org.jetbrains.annotations.NotNull;

public record ResolvedFeedbackView (
        @NotNull FeedbackView feedback,
        @NotNull Long replyTo
) {
    public ResolvedFeedbackView(ResolvedFeedback resolvedFeedback) {
        this(
                new FeedbackView(resolvedFeedback.feedback()),
                resolvedFeedback.replyTo().value()
        );
    }
}
