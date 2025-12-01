package com.plux.distribution.core.feedback.application.dto;

import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import org.jetbrains.annotations.NotNull;

public record ResolvedFeedback(
        @NotNull Feedback feedback,
        @NotNull InteractionDto replyTo
) {

}
