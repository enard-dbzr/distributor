package com.plux.distribution.application.dto.feedback.dto.payload;

import com.plux.distribution.application.dto.message.MessageDto;
import org.jetbrains.annotations.NotNull;

public record MessagePayload(
        MessageDto content
) implements FeedbackPayload {


    @Override
    public <R> R accept(@NotNull FeedbackPayloadVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
