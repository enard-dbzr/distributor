package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import org.jetbrains.annotations.NotNull;

public record ReplyFeedbackPayloadView(
        @NotNull Long replyTo
//        @NotNull Long contentMessageId
) implements FeedbackPayloadView {

}
