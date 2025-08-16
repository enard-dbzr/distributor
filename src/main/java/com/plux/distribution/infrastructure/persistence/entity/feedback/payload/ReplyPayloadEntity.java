package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

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
}
