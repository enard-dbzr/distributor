package com.plux.distribution.infrastructure.notifier.view.feedback.payload;

import com.plux.distribution.infrastructure.notifier.view.interaction.InteractionView;
import org.jetbrains.annotations.NotNull;

public record MessageFeedbackPayloadView(
        @NotNull InteractionView content
) implements FeedbackPayloadView {

}
