package com.plux.distribution.core.workflow.domain.frame;

import com.plux.distribution.core.feedback.domain.Feedback;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public record FrameFeedback(
        @NotNull Feedback feedback,
        @NotNull InteractionContent content,
        @NotNull Optional<String> text,
        @NotNull Optional<String> buttonTag
) {

}
