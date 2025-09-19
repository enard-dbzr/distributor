package com.plux.distribution.infrastructure.notifier.view.feedback;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.feedback.domain.payload.ButtonPayload;
import com.plux.distribution.core.feedback.domain.payload.FeedbackPayloadVisitor;
import com.plux.distribution.core.feedback.domain.payload.MessagePayload;
import com.plux.distribution.core.feedback.domain.payload.ReplyPayload;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.ButtonFeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.FeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.MessageFeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.ReplyFeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.message.MessageView;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class FeedbackView {

    private final long id;
    private final Date actionTime;
    private final long chatId;
    private final FeedbackPayloadView payload;

    public FeedbackView(Feedback model) {
        var payloadView = model.payload().accept(new FeedbackPayloadVisitor<FeedbackPayloadView>() {
            @Override
            public FeedbackPayloadView visit(@NotNull ButtonPayload entity) {
                return new ButtonFeedbackPayloadView(entity.replyTo().value(), entity.tag());
            }

            @Override
            public FeedbackPayloadView visit(@NotNull MessagePayload entity) {
                return new MessageFeedbackPayloadView(new MessageView(entity.content()));
            }

            @Override
            public FeedbackPayloadView visit(@NotNull ReplyPayload entity) {
                return new ReplyFeedbackPayloadView(entity.replyTo().value(), new MessageView(entity.content()));
            }
        });

        this.id = model.id().value();
        this.actionTime = model.actionTime();
        this.chatId = model.chatId().value();
        this.payload = payloadView;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
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
    public FeedbackPayloadView getPayload() {
        return payload;
    }
}
