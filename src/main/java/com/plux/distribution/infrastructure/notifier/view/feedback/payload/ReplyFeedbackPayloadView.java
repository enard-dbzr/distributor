package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import com.plux.distribution.infrastructure.notifier.view.message.MessageView;
import org.jetbrains.annotations.NotNull;

public record ReplyFeedbackPayloadView(
        @NotNull Long replyTo,
        @NotNull MessageView content
) implements FeedbackPayloadView {

}
