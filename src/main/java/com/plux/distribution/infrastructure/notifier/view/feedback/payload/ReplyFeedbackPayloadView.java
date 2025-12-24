package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import com.plux.distribution.infrastructure.notifier.view.interaction.InteractionView;
import org.jetbrains.annotations.NotNull;

public record ReplyFeedbackPayloadView(
        @NotNull Long replyTo,
        @NotNull InteractionView content
) implements FeedbackPayloadView {

}
