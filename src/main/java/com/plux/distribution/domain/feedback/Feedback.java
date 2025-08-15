package com.plux.distribution.domain.feedback;

import com.plux.distribution.domain.feedback.payload.FeedbackPayload;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
import java.util.Date;

public record Feedback(Date actionTime, UserId userId, FeedbackPayload payload) {
    MessageId getReplyTo() {
        return payload.getReplyTo();
    }
}
