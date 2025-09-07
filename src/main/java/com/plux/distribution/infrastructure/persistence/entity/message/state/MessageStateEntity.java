package com.plux.distribution.infrastructure.persistence.entity.message.state;

import com.plux.distribution.domain.message.state.MessageState;
import com.plux.distribution.domain.message.state.MessageStateVisitor;
import com.plux.distribution.domain.message.state.PendingState;
import com.plux.distribution.domain.message.state.ReceivedState;
import com.plux.distribution.domain.message.state.TransferredState;
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
@Table(name = "message_states")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class MessageStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract MessageState toModel();

    public static MessageStateEntity fromModel(MessageState state) {
        return state.accept(new MessageStateVisitor<MessageStateEntity>() {
            @Override
            public MessageStateEntity visit(PendingState state) {
                return PendingStateEntity.fromModel(state);
            }

            @Override
            public MessageStateEntity visit(TransferredState state) {
                return TransferredStateEntity.fromModel(state);
            }

            @Override
            public MessageStateEntity visit(ReceivedState state) {
                return ReceivedStateEntity.fromModel(state);
            }
        });
    }
}
