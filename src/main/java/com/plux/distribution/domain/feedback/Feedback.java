package com.plux.distribution.domain.feedback;

import com.plux.distribution.domain.feedback.payload.FeedbackPayload;
import com.plux.distribution.domain.chat.ChatId;
import java.util.Date;

public record Feedback(
        FeedbackId id,
        Date actionTime,
        ChatId chatId,
        FeedbackPayload payload
) {

}
