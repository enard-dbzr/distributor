package com.plux.distribution.core.feedback.domain;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import java.util.Date;

public record Feedback(
        Date actionTime,
        ChatId chatId,
        InteractionDto payload
) {

}
