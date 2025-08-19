package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.workflow.ContextLinkerPort;
import com.plux.distribution.application.port.out.workflow.ContextLoaderPort;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameContextManager;
import com.plux.distribution.application.workflow.core.FrameFactory;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.infrastructure.persistence.entity.workflow.FrameContextSnapshotEntity;
import com.plux.distribution.infrastructure.persistence.serializer.ContextSnapshotSerializer;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbFrameLinker implements ContextLinkerPort, ContextLoaderPort {
    private final SessionFactory sessionFactory;
    private FrameContextManager manager;
    private final FrameFactory frameFactory;

    public DbFrameLinker(SessionFactory sessionFactory, FrameFactory frameFactory) {
        this.sessionFactory = sessionFactory;
        this.frameFactory = frameFactory;
    }

    @Override
    public void link(@NotNull FrameContext context, @NotNull MessageId messageId) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var snapshot = context.save();

            var entity = new FrameContextSnapshotEntity(
                    context.getChatId().value(),
                    ContextSnapshotSerializer.toJson(snapshot)
            );

            session.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public @NotNull Optional<FrameContext> load(@NotNull ChatId chatId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(FrameContextSnapshotEntity.class, chatId.value());
            if (entity == null) {
                return Optional.empty();
            }

            var context = new FrameContext(manager, frameFactory, chatId);
            context.restore(ContextSnapshotSerializer.fromJson(entity.getSnapshot()));

            return Optional.of(context);
        }
    }

    @Override
    public @NotNull FrameContext init(ChatId chatId) {
        return new FrameContext(manager, frameFactory, chatId);
    }

    public void setManager(FrameContextManager manager) {
        this.manager = manager;
    }
}
