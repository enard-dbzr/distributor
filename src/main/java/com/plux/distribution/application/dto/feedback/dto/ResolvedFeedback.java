package com.plux.distribution.application.dto.feedback.dto;

import com.plux.distribution.application.dto.message.MessageDto;
import org.jetbrains.annotations.NotNull;

public record ResolvedFeedback(
        @NotNull Feedback feedback,
        @NotNull MessageDto replyTo
) {

}
