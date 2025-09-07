package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import org.jetbrains.annotations.NotNull;

public record MessageFeedbackPayloadView(
        @NotNull Long contentMessageId
) implements FeedbackPayloadView {

}
