package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.workflow.ContextRepositoryPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.infrastructure.persistence.entity.workflow.FrameContextSnapshotEntity;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DbFrameRepository implements ContextRepositoryPort {

    private final SessionFactory sessionFactory;

    public DbFrameRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(@NotNull ChatId chatId, @NotNull String snapshot) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new FrameContextSnapshotEntity(
                    chatId.value(),
                    snapshot
            );

            session.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public @Nullable String getSnapshot(@NotNull ChatId chatId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(FrameContextSnapshotEntity.class, chatId.value());
            if (entity == null) {
                return null;
            }

            return entity.getSnapshot();
        }
    }
}
