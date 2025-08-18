package com.plux.distribution.domain.feedback;

import com.plux.distribution.domain.feedback.payload.FeedbackPayload;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.chat.ChatId;
import java.util.Date;

public record Feedback(Date actionTime, ChatId chatId, FeedbackPayload payload) {
    MessageId getReplyTo() {
        return payload.getReplyTo();
    }
}
