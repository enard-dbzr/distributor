package com.plux.distribution.infrastructure.persistence.entity.interaction.content;

import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("REPLY")
public class ReplyContentEntity extends InteractionContentEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "original_id")
    private InteractionContentEntity original;

    private Long replyTo;

    public ReplyContentEntity(InteractionContentEntity original, Long replyTo) {
        this.original = original;
        this.replyTo = replyTo;
    }

    public ReplyContentEntity() {

    }

    @Override
    public InteractionContent toModel() {
        return new ReplyMessageContent(
                original.toModel(),
                new InteractionId(replyTo)
        );
    }
}
