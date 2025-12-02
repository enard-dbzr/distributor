package com.plux.distribution.infrastructure.notifier.view.feedback;

import com.plux.distribution.core.feedback.domain.Feedback;
import com.plux.distribution.infrastructure.notifier.view.interaction.InteractionView;
import java.util.Date;

public class FeedbackView {

    private final Date actionTime;
    private final long chatId;
    private final InteractionView payload;

    public FeedbackView(Feedback model) {
        this.actionTime = model.actionTime();
        this.chatId = model.chatId().value();
        this.payload = new InteractionView(model.payload());
    }

    @SuppressWarnings("unused")
    public Date getActionTime() {
        return actionTime;
    }

    @SuppressWarnings("unused")
    public long getChatId() {
        return chatId;
    }

    @SuppressWarnings("unused")
    public InteractionView getPayload() {
        return payload;
    }
}
