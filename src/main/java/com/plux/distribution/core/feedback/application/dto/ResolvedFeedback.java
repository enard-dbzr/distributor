package com.plux.distribution.core.feedback.application.dto;

import com.plux.distribution.core.message.application.dto.MessageDto;
import org.jetbrains.annotations.NotNull;

public record ResolvedFeedback(
        @NotNull Feedback feedback,
        @NotNull MessageDto replyTo
) {

}
