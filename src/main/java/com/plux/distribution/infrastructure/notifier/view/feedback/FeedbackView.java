package com.plux.distribution.infrastructure.notifier.view.feedback;

import com.plux.distribution.application.dto.feedback.dto.Feedback;
import com.plux.distribution.application.dto.feedback.dto.payload.ButtonPayload;
import com.plux.distribution.application.dto.feedback.dto.payload.FeedbackPayloadVisitor;
import com.plux.distribution.application.dto.feedback.dto.payload.MessagePayload;
import com.plux.distribution.application.dto.feedback.dto.payload.ReplyPayload;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.ButtonFeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.FeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.MessageFeedbackPayloadView;
import com.plux.distribution.infrastructure.notifier.view.feedback.payload.ReplyFeedbackPayloadView;
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
                return new MessageFeedbackPayloadView();  // FIXME: add content message view
            }

            @Override
            public FeedbackPayloadView visit(@NotNull ReplyPayload entity) {
                return new ReplyFeedbackPayloadView(entity.replyTo().value());  // FIXME: add content message view
            }
        });

        this.id = model.id().value();
        this.actionTime = model.actionTime();
        this.chatId = model.chatId().value();
        this.payload = payloadView;
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
