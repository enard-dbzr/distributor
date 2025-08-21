package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.FeedbackPayload;
import com.plux.distribution.domain.message.MessageId;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public @NotNull FeedbackPayload toModel() {
        return new ButtonPayload(new MessageId(replyToId), tag);
    }
}
