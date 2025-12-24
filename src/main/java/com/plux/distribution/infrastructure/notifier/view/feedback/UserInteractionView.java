package com.plux.distribution.infrastructure.notifier.view.feedback;

import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.session.application.dto.SessionDto;
import com.plux.distribution.infrastructure.notifier.view.session.SessionView;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record UserInteractionView(
        @NotNull FeedbackView feedback,
        @NotNull Long replyTo,
        @Nullable SessionView session
) {

    public UserInteractionView(ResolvedFeedback resolvedFeedback, SessionDto session) {
        this(
                new FeedbackView(resolvedFeedback.feedback()),
                resolvedFeedback.replyTo().id().value(),
                Optional.ofNullable(session).map(SessionView::new).orElse(null)
        );
    }
}
