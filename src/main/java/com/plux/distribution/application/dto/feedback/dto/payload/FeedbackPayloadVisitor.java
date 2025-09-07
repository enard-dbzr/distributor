package com.plux.distribution.application.dto.feedback.dto.payload;

import org.jetbrains.annotations.NotNull;

public interface FeedbackPayloadVisitor<R> {

    R visit(@NotNull ButtonPayload entity);

    R visit(@NotNull MessagePayload entity);

    R visit(@NotNull ReplyPayload entity);
}
