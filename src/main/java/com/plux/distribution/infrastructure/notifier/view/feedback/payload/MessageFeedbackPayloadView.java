package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import com.plux.distribution.infrastructure.notifier.view.message.MessageView;
import org.jetbrains.annotations.NotNull;

public record MessageFeedbackPayloadView(
        @NotNull MessageView content
) implements FeedbackPayloadView {

}
