package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public record FrameFeedback(
        @NotNull Feedback feedback,
        @NotNull Optional<InteractionContent> content,
        @NotNull Optional<String> text,
        @NotNull Optional<String> buttonTag
) {

}
