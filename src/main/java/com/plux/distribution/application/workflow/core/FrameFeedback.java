package com.plux.distribution.application.workflow.core;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.message.Message;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public record FrameFeedback(
        @NotNull Feedback feedback,
        @NotNull Optional<Message> content,
        @NotNull Optional<String> text,
        @NotNull Optional<String> buttonTag
) {

}
