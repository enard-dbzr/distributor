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
import java.util.concurrent.atomic.AtomicReference;

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
        var holder = new AtomicReference<MessageStateEntity>();

        state.accept(new MessageStateVisitor() {
            @Override
            public void visit(PendingState state) {
                holder.set(PendingStateEntity.fromModel(state));
            }

            @Override
            public void visit(TransferredState state) {
                holder.set(TransferredStateEntity.fromModel(state));
            }

            @Override
            public void visit(ReceivedState state) {
                holder.set(ReceivedStateEntity.fromModel(state));
            }
        });

        return holder.get();
    }
}
