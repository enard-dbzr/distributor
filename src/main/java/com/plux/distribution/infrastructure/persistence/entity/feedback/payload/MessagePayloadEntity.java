package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.domain.feedback.payload.FeedbackPayload;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.message.MessageId;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.jetbrains.annotations.NotNull;

@Entity
@DiscriminatorValue("MESSAGE")
public class MessagePayloadEntity extends FeedbackPayloadEntity {

    private Long contentId;

    public static MessagePayloadEntity fromModel(MessagePayload model) {
        MessagePayloadEntity entity = new MessagePayloadEntity();

        entity.contentId = model.content().value();

        return entity;
    }

    @Override
    public @NotNull FeedbackPayload toModel() {
        return new MessagePayload(new MessageId(contentId));
    }
}
