package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.application.port.out.InteractionRepositoryPort;
import com.plux.distribution.core.interaction.domain.Interaction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.infrastructure.persistence.entity.interaction.InteractionEntity;
import com.plux.distribution.infrastructure.persistence.entity.interaction.state.InteractionStateEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DbInteractionRepository implements InteractionRepositoryPort {

    private static final Logger log = LogManager.getLogger(DbInteractionRepository.class);
    private final SessionFactory sessionFactory;

    public DbInteractionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull Interaction create(@NotNull CreateInteractionCommand command) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new InteractionEntity(command);
            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }

    @Override
    public void update(@NotNull Interaction message) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = session.find(InteractionEntity.class, message.getId().value());

            if (!entity.getState().toModel().equals(message.getState())) {
                entity.setState(InteractionStateEntity.fromModel(message.getState()));
            }

            transaction.commit();

        }
    }

    @Override
    public @NotNull Interaction get(@NotNull InteractionId interactionId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(InteractionEntity.class, interactionId.value());

            return entity.toModel();
        }
    }

    @Override
    public @Nullable Interaction getLastOfRecipient(@NotNull Participant recipient) {

        try (var session = sessionFactory.openSession()) {
            var query = switch (recipient) {
                case ChatParticipant p -> {
                    var q = session.createQuery("from InteractionEntity m "
                            + "join ChatParticipantEntity p on m.recipient = p "
                            + "where p.chatId = :cid "
                            + "order by m.id desc "
                            + "fetch first 1 rows only", InteractionEntity.class);

                    q.setParameter("cid", p.chatId().value());

                    yield q;
                }
                case BotParticipant _ -> session.createQuery("from InteractionEntity m "
                        + "join BotParticipantEntity p on m.recipient = p "
                        + "order by m.id desc "
                        + "fetch first 1 rows only", InteractionEntity.class);

            };

            var messageEntity = query.uniqueResult();

            if (messageEntity == null) {
                log.warn("Last message with recipient={} not found", recipient);
                return null;
            }

            return messageEntity.toModel();
        }
    }
}
