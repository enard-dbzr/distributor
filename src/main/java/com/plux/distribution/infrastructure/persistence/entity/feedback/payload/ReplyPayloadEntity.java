package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.domain.feedback.payload.FeedbackPayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import com.plux.distribution.domain.message.MessageId;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.jetbrains.annotations.NotNull;

@Entity
@DiscriminatorValue("REPLY")
public class ReplyPayloadEntity extends FeedbackPayloadEntity {

    private Long replyToId;
    private Long contentId;

    public static ReplyPayloadEntity fromModel(ReplyPayload model) {
        ReplyPayloadEntity entity = new ReplyPayloadEntity();

        entity.replyToId = model.replyTo().value();
        entity.contentId = model.content().value();

        return entity;
    }

    @Override
    public @NotNull FeedbackPayload toModel() {
        return new ReplyPayload(new MessageId(replyToId), new MessageId(contentId));
    }
}
