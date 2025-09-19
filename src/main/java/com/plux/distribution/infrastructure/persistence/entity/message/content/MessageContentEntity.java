package com.plux.distribution.infrastructure.persistence.entity.message.content;

import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.content.MessageContentVisitor;
import com.plux.distribution.core.message.domain.content.ReplyMessageContent;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
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
@Table(name = "message_contents")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class MessageContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private Long id;

    public static MessageContentEntity fromModel(MessageContent model) {
        return model.accept(new MessageContentVisitor<>() {
            @Override
            public MessageContentEntity visit(SimpleMessageContent content) {
                return SimpleContentEntity.fromModel(content);
            }

            @Override
            public MessageContentEntity visit(ReplyMessageContent content) {
                return ReplyContentEntity.fromModel(content);
            }
        });
    }

    public abstract MessageContent toModel();

}
