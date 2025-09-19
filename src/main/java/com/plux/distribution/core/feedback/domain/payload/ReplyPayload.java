package com.plux.distribution.core.feedback.domain.payload;

import com.plux.distribution.core.message.application.dto.MessageDto;
import com.plux.distribution.core.message.domain.MessageId;
import org.jetbrains.annotations.NotNull;

public record ReplyPayload(
        MessageId replyTo,
        MessageDto content
) implements FeedbackPayload {


    @Override
    public <R> R accept(@NotNull FeedbackPayloadVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
