package com.plux.distribution.domain.feedback.payload;

import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public record MessagePayload(MessageId content) implements FeedbackPayload {

    @Override
    public void accept(@NotNull FeedbackPayloadVisitor visitor) {
        visitor.visit(this);
    }

}
