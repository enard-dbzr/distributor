package com.plux.distribution.core.feedback.application.command;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.domain.payload.FeedbackPayload;
import java.util.Date;

public record CreateFeedbackCommand(
        Date actionTime,
        ChatId chatId,
        FeedbackPayload payload
) {

}
