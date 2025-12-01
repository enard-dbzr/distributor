package com.plux.distribution.core.feedback.domain.payload;

import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import org.jetbrains.annotations.NotNull;

public record MessagePayload(
        InteractionDto content
) implements FeedbackPayload {


    @Override
    public <R> R accept(@NotNull FeedbackPayloadVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
