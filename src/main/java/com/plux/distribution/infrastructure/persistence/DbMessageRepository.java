package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.message.CreateMessagePort;
import com.plux.distribution.application.port.out.message.UpdateMessagePort;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.infrastructure.persistence.entity.message.MessageEntity;
import com.plux.distribution.infrastructure.persistence.entity.message.participant.ParticipantEntity;
import com.plux.distribution.infrastructure.persistence.entity.message.state.MessageStateEntity;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbMessageRepository implements CreateMessagePort, UpdateMessagePort {
    private final SessionFactory sessionFactory;

    public DbMessageRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull MessageId create(@NotNull Message message) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = MessageEntity.fromModel(message);
            session.persist(entity);

            transaction.commit();

            return new MessageId(entity.getId());
        }
    }

    @Override
    public void update(@NotNull MessageId id, @NotNull Message message) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = session.find(MessageEntity.class, id.value());

            if (!entity.getRecipient().toModel().equals(message.getRecipient())) {
                entity.setRecipient(ParticipantEntity.fromModel(message.getRecipient()));
            }
            if (!entity.getState().toModel().equals(message.getState())) {
                entity.setState(MessageStateEntity.fromModel(message.getState()));
            }

            transaction.commit();

        }
    }
}
