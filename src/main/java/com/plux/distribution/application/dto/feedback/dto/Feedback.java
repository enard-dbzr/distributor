package com.plux.distribution.application.dto.feedback.dto;

import com.plux.distribution.application.dto.feedback.dto.payload.FeedbackPayload;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.feedback.FeedbackId;
import java.util.Date;

public record Feedback(
        FeedbackId id,
        Date actionTime,
        ChatId chatId,
        FeedbackPayload payload
) {

}
