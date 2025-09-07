package com.plux.distribution.infrastructure.notifier.view.feedback;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.FeedbackPayloadVisitor;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.ButtonFeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.FeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.MessageFeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.ReplyFeedbackPayloadView;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class FeedbackView {

    private final long id;
    private final Date actionTime;
    private final long chatId;
    private final FeedbackPayloadView payload;

    public FeedbackView(Feedback model) {
        var payloadView = new AtomicReference<FeedbackPayloadView>();

        model.payload().accept(new FeedbackPayloadVisitor() {
            @Override
            public void visit(@NotNull ButtonPayload entity) {
                payloadView.set(new ButtonFeedbackPayloadView(entity.replyTo().value(), entity.tag()));
            }

            @Override
            public void visit(@NotNull MessagePayload entity) {
                payloadView.set(new MessageFeedbackPayloadView(entity.content().value()));
            }

            @Override
            public void visit(@NotNull ReplyPayload entity) {
                payloadView.set(new ReplyFeedbackPayloadView(entity.replyTo().value(), entity.content().value()));
            }
        });

        this.id = model.id().value();
        this.actionTime = model.actionTime();
        this.chatId = model.chatId().value();
        this.payload = payloadView.get();
    }

    public long getId() {
        return id;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public long getChatId() {
        return chatId;
    }

    public FeedbackPayloadView getPayload() {
        return payload;
    }
}
