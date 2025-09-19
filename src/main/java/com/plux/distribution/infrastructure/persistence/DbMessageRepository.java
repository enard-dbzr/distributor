package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.message.application.command.CreateMessageCommand;
import com.plux.distribution.core.message.application.port.out.CreateMessagePort;
import com.plux.distribution.core.message.application.port.out.GetMessagePort;
import com.plux.distribution.core.message.application.port.out.UpdateMessagePort;
import com.plux.distribution.core.message.domain.MessageModel;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.participant.ChatParticipant;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.participant.ParticipantVisitor;
import com.plux.distribution.core.message.domain.participant.SelfParticipant;
import com.plux.distribution.core.message.domain.participant.ServiceParticipant;
import com.plux.distribution.core.message.domain.participant.UnknownServiceParticipant;
import com.plux.distribution.infrastructure.persistence.entity.message.MessageEntity;
import com.plux.distribution.infrastructure.persistence.entity.message.participant.ParticipantEntity;
import com.plux.distribution.infrastructure.persistence.entity.message.state.MessageStateEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DbMessageRepository implements CreateMessagePort, UpdateMessagePort, GetMessagePort {

    private static final Logger log = LogManager.getLogger(DbMessageRepository.class);
    private final SessionFactory sessionFactory;

    public DbMessageRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull MessageModel create(@NotNull CreateMessageCommand command) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new MessageEntity(command);
            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }

    @Override
    public void update(@NotNull MessageModel message) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = session.find(MessageEntity.class, message.getId().value());

            if (!entity.getRecipient().toModel().equals(message.getRecipient())) {
                entity.setRecipient(ParticipantEntity.fromModel(message.getRecipient()));
            }
            if (!entity.getState().toModel().equals(message.getState())) {
                entity.setState(MessageStateEntity.fromModel(message.getState()));
            }

            transaction.commit();

        }
    }

    @Override
    public @NotNull MessageModel get(@NotNull MessageId messageId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(MessageEntity.class, messageId.value());

            return entity.toModel();
        }
    }

    @Override
    public @Nullable MessageModel getLastOfRecipient(@NotNull Participant recipient) {

        try (var session = sessionFactory.openSession()) {
            var query = recipient.accept(new ParticipantVisitor<Query<MessageEntity>>() {
                @Override
                public Query<MessageEntity> visit(ServiceParticipant participant) {
                    var q = session.createQuery("from MessageEntity m "
                            + "join ServiceParticipantEntity p on m.recipient = p "
                            + "where p.serviceId = :sid "
                            + "order by m.id desc "
                            + "fetch first 1 rows only", MessageEntity.class);

                    q.setParameter("sid", participant.serviceId().value());

                    return q;
                }

                @Override
                public Query<MessageEntity> visit(UnknownServiceParticipant participant) {
                    return session.createQuery("from MessageEntity m "
                            + "join UnkServiceParticipantEntity p on m.recipient = p "
                            + "order by m.id desc "
                            + "fetch first 1 rows only", MessageEntity.class);
                }

                @Override
                public Query<MessageEntity> visit(ChatParticipant participant) {
                    var q = session.createQuery("from MessageEntity m "
                            + "join ChatParticipantEntity p on m.recipient = p "
                            + "where p.chatId = :cid "
                            + "order by m.id desc "
                            + "fetch first 1 rows only", MessageEntity.class);

                    q.setParameter("cid", participant.chatId().value());
                    return q;
                }

                @Override
                public Query<MessageEntity> visit(SelfParticipant participant) {
                    return session.createQuery("from MessageEntity m "
                            + "join SelfParticipantEntity p on m.recipient = p "
                            + "order by m.id desc "
                            + "fetch first 1 rows only", MessageEntity.class);
                }
            });

            var messageEntity = query.uniqueResult();

            if (messageEntity == null) {
                log.warn("Last message with recipient={} not found", recipient);
                return null;
            }

            return messageEntity.toModel();
        }
    }
}
