package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import org.jetbrains.annotations.NotNull;

public record ButtonFeedbackPayloadView(
        @NotNull Long replyTo,
        @NotNull String tag
) implements FeedbackPayloadView {

}
