package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.application.port.out.session.SaveSessionPort;
import com.plux.distribution.domain.session.Session;
import com.plux.distribution.infrastructure.persistence.entity.session.SessionEntity;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbSessionRepository implements SaveSessionPort {
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
}
