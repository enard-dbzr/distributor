package com.plux.distribution.application.dto.feedback.dto.payload;

import com.plux.distribution.domain.message.MessageId;
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
