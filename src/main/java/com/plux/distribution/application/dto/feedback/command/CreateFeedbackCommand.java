package com.plux.distribution.application.dto.feedback.command;

import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.application.dto.feedback.dto.payload.FeedbackPayload;
import java.util.Date;

public record CreateFeedbackCommand(
        Date actionTime,
        ChatId chatId,
        FeedbackPayload payload
) {

}
