package com.plux.distribution.core.feedback.domain.payload;

import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public record ButtonPayload(
        InteractionId replyTo,
        String tag
) implements FeedbackPayload {


    @Override
    public <R> R accept(@NotNull FeedbackPayloadVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
