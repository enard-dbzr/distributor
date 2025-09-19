package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.core.feedback.domain.payload.MessagePayload;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MESSAGE")
public class MessagePayloadEntity extends FeedbackPayloadEntity {

    @SuppressWarnings("unused")
    private Long contentId;

    public static MessagePayloadEntity fromModel(MessagePayload model) {
        MessagePayloadEntity entity = new MessagePayloadEntity();

        entity.contentId = model.content().id().value();

        return entity;
    }
}
