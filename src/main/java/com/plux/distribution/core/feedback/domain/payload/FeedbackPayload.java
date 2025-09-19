package com.plux.distribution.core.feedback.domain.payload;

import org.jetbrains.annotations.NotNull;

public sealed interface FeedbackPayload permits ButtonPayload, MessagePayload, ReplyPayload {

    <R> R accept(@NotNull FeedbackPayloadVisitor<R> visitor);
}
