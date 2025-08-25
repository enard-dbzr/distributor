package com.plux.distribution.infrastructure.persistence.entity.message.content;

import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.content.MessageContentVisitor;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Table(name = "message_contents")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class MessageContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public static MessageContentEntity fromModel(MessageContent model) {
        var holder = new AtomicReference<MessageContentEntity>();

        model.accept(new MessageContentVisitor() {
            @Override
            public void visit(SimpleMessageContent content) {
                holder.set(SimpleContentEntity.fromModel(content));
            }
        });

        return holder.get();
    }

    public abstract MessageContent toModel();

}
