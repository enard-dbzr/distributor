package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.workflow.SaveContextPort;
import com.plux.distribution.application.port.out.workflow.LoadContextPort;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameContextManager;
import com.plux.distribution.application.workflow.core.FrameFactory;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.infrastructure.persistence.entity.workflow.FrameContextSnapshotEntity;
import com.plux.distribution.infrastructure.persistence.serializer.ContextSnapshotSerializer;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbFrameRepository implements SaveContextPort, LoadContextPort {

    private final SessionFactory sessionFactory;
    private final ContextSnapshotSerializer snapshotSerializer = new ContextSnapshotSerializer();

    public DbFrameRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(@NotNull FrameContext context) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var snapshot = context.save();

            var entity = new FrameContextSnapshotEntity(
                    context.getChatId().value(),
                    snapshotSerializer.toJson(snapshot)
            );

            session.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public @NotNull Optional<FrameContext> load(@NotNull ChatId chatId, @NotNull FrameContextManager manager,
            @NotNull FrameFactory frameFactory) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(FrameContextSnapshotEntity.class, chatId.value());
            if (entity == null) {
                return Optional.empty();
            }

            var context = new FrameContext(manager, frameFactory, chatId);
            context.restore(snapshotSerializer.fromJson(entity.getSnapshot()));

            return Optional.of(context);
        }
    }
}
