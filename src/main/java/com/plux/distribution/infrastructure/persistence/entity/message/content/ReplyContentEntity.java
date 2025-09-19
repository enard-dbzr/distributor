package com.plux.distribution.infrastructure.persistence.entity.message.content;

import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.content.ReplyMessageContent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("REPLY")
public class ReplyContentEntity extends MessageContentEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "original_id")
    private MessageContentEntity original;

    private Long replyTo;

    public static ReplyContentEntity fromModel(ReplyMessageContent model) {
        ReplyContentEntity entity = new ReplyContentEntity();

        entity.original = MessageContentEntity.fromModel(model.original());
        entity.replyTo = model.replyTo().value();

        return entity;
    }

    @Override
    public MessageContent toModel() {
        return new ReplyMessageContent(
                original.toModel(),
                new MessageId(replyTo)
        );
    }
}
