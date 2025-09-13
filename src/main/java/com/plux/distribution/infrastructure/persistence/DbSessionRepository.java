package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.application.port.out.session.SessionRepositoryPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.domain.session.Session;
import com.plux.distribution.domain.session.SessionState;
import com.plux.distribution.infrastructure.persistence.entity.session.SessionEntity;
import java.util.Collection;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DbSessionRepository implements SessionRepositoryPort {

    private final SessionFactory sessionFactory;

    public DbSessionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull Session create(@NotNull CreateSessionCommand command) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new SessionEntity(command);
            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }

    @Override
    public void update(@NotNull Session model) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new SessionEntity(model);
            session.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public @Nullable Session findWithStates(@NotNull ChatId chatId, @NotNull ServiceId serviceId,
            Collection<SessionState> sessionStates) {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery("from SessionEntity "
                    + "where chatId=:chatId and serviceId=:serviceId "
                    + "and state in :sessionStates "
                    + "order by id desc "
                    + "fetch first 1 rows only", SessionEntity.class);
            query.setParameter("chatId", chatId.value());
            query.setParameter("serviceId", serviceId.value());
            query.setParameter("sessionStates", sessionStates);

            var entity = query.uniqueResult();

            return entity == null ? null : entity.toModel();
        }
    }
}
