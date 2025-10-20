package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.session.application.port.out.SessionInteractionsRepositoryPort;
import com.plux.distribution.core.session.domain.SessionId;
import com.plux.distribution.infrastructure.persistence.entity.session.SessionInteractionsCountEntity;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DbSessionInteractionsRepository implements SessionInteractionsRepositoryPort {

    private final @NotNull SessionFactory sessionFactory;

    public DbSessionInteractionsRepository(
            @NotNull SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    @Override
    public void setLeftInteractionsCount(@NotNull SessionId sessionId, @Nullable Integer count) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new SessionInteractionsCountEntity(sessionId.value(), count);

            session.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public @Nullable Integer getLeftInteractionsCount(@NotNull SessionId sessionId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(SessionInteractionsCountEntity.class, sessionId.value());

            return Optional.ofNullable(entity).map(SessionInteractionsCountEntity::getCount).orElse(null);
        }
    }
}
