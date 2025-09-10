package com.plux.distribution.application.dto.feedback.dto;

import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public record ResolvedFeedback(
        @NotNull Feedback feedback,
        @NotNull MessageId replyTo
) {

}
