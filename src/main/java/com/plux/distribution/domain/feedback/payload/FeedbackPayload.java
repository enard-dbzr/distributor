package com.plux.distribution.domain.feedback.payload;

import org.jetbrains.annotations.NotNull;

public sealed interface FeedbackPayload permits ButtonPayload, MessagePayload, ReplyPayload {

    void accept(@NotNull FeedbackPayloadVisitor visitor);
}
