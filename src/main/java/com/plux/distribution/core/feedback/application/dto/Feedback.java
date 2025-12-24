package com.plux.distribution.core.feedback.application.dto;

import com.plux.distribution.core.feedback.domain.payload.FeedbackPayload;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.domain.FeedbackId;
import java.util.Date;

public record Feedback(
        FeedbackId id,
        Date actionTime,
        ChatId chatId,
        FeedbackPayload payload
) {

}
