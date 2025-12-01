package com.plux.distribution.core.feedback.domain.payload;

import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public record ReplyPayload(
        InteractionId replyTo,
        InteractionDto content
) implements FeedbackPayload {


    @Override
    public <R> R accept(@NotNull FeedbackPayloadVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
