package com.plux.distribution.domain.feedback.payload;

import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public record ButtonPayload(MessageId replyTo, String tag) implements FeedbackPayload {

    @Override
    public void accept(@NotNull FeedbackPayloadVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public MessageId getReplyTo() {
        return replyTo;
    }
}
