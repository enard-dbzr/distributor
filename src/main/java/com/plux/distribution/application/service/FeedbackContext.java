package com.plux.distribution.application.service;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.message.content.MessageContent;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public record FeedbackContext(
        @NotNull Feedback feedback,
        @NotNull Optional<MessageContent> content
) {

}
