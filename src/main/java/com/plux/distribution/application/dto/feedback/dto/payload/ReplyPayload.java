package com.plux.distribution.application.dto.feedback.dto.payload;

import com.plux.distribution.application.dto.message.MessageDto;
import com.plux.distribution.domain.message.MessageId;
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
