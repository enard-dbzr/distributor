package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BUTTON")
public class ButtonPayloadEntity extends FeedbackPayloadEntity {

    private Long replyToId;
    private String tag;

    public static ButtonPayloadEntity fromModel(ButtonPayload model) {
        ButtonPayloadEntity entity = new ButtonPayloadEntity();

        entity.replyToId = model.replyTo().value();
        entity.tag = model.tag();

        return entity;
    }
}
