package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.core.feedback.domain.payload.ButtonPayload;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUTTON")
public class ButtonPayloadEntity extends FeedbackPayloadEntity {

    @SuppressWarnings("unused")
    private Long replyToId;

    @SuppressWarnings("unused")
    private String tag;

    public static ButtonPayloadEntity fromModel(ButtonPayload model) {
        ButtonPayloadEntity entity = new ButtonPayloadEntity();

        entity.replyToId = model.replyTo().value();
        entity.tag = model.tag();

        return entity;
    }
}
