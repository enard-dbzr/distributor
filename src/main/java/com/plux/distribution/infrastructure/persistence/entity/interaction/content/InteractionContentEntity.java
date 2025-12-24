package com.plux.distribution.infrastructure.persistence.entity.interaction.content;

import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.persistence.entity.interaction.attachment.AttachmentEntity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "interaction_contents")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class InteractionContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private Long id;

    public static InteractionContentEntity fromModel(InteractionContent model) {
        return switch (model) {
            case SimpleMessageContent c -> new SimpleContentEntity(
                    c.text(),
                    c.attachments().stream()
                            .map(AttachmentEntity::fromModel)
                            .toList()
            );
            case ReplyMessageContent c -> new ReplyContentEntity(fromModel(c.original()), c.replyTo().value());
            case ButtonClickContent c -> new ButtonClickContentEntity(c.tag(), c.source().value());
        };
    }

    public abstract InteractionContent toModel();

}
