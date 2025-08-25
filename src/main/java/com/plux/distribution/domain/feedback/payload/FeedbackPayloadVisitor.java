package com.plux.distribution.domain.feedback.payload;

import org.jetbrains.annotations.NotNull;

public interface FeedbackPayloadVisitor {

    void visit(@NotNull ButtonPayload entity);

    void visit(@NotNull MessagePayload entity);

    void visit(@NotNull ReplyPayload entity);
}
