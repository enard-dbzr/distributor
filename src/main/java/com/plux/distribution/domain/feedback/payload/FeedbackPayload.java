package com.plux.distribution.domain.feedback.payload;

import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface FeedbackPayload {
    void accept(@NotNull FeedbackPayloadVisitor visitor);
    MessageId getReplyTo();
}
