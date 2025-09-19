package com.plux.distribution.core.feedback.domain.payload;

import com.plux.distribution.core.message.domain.MessageId;
import org.jetbrains.annotations.NotNull;

public record ButtonPayload(
        MessageId replyTo,
        String tag
) implements FeedbackPayload {


    @Override
    public <R> R accept(@NotNull FeedbackPayloadVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
