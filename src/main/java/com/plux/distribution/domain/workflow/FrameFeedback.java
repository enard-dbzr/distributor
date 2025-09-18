package com.plux.distribution.domain.workflow;

import com.plux.distribution.application.dto.feedback.dto.Feedback;
import com.plux.distribution.domain.message.content.MessageContent;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public record FrameFeedback(
        @NotNull Feedback feedback,
        @NotNull Optional<MessageContent> content,
        @NotNull Optional<String> text,
        @NotNull Optional<String> buttonTag
) {

}
